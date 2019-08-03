package com.luo.dubbo.rpc.transport;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;
import com.luo.dubbo.util.Assert;
import com.luo.dubbo.util.ThreadUtils;

public final class NettyClient extends NettyBootstrap {
    private static Logger log = Logger.getLogger(NettyClient.class);
    private volatile boolean isRuning = false;
    private final Lock lock = new ReentrantLock();
    private Socket socket;

    private Object startedLock = new Object();

    public NettyClient(@SuppressWarnings("rawtypes") MessageListener messageListener) {
        super(messageListener);
        Assert.isNull(messageListener, "消息接收器不能为空");
    }

    /***
     * 心跳字符
     */

    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();

    @Override
    public void connect(String host, int port, ConnectMonitor connectMonitor) throws Exception {
        setConnectMonitor(connectMonitor);
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap boot = new Bootstrap();
        boot.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));
        ChannelFuture future;
        // 进行连接
        try {
            synchronized (boot) {
                boot.handler(new ChannelInitializer<Channel>() {
                    // 初始化channel
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        /***
                         * 指定字符 解码器
                         */
                        ch.pipeline().addLast(
                                new DelimiterBasedFrameDecoder(1024 * 1024 * 10, Unpooled
                                        .copiedBuffer(NEW_LINE.getBytes())));
                        ch.pipeline().addLast(new IdleStateHandler(0, hear_time, 0, TimeUnit.SECONDS),
                                idleStateTrigger, new StringDecoder(), new StringEncoder(),
                                new HeartBeatClientHandler());
                    }
                });

                future = boot.connect(host, port);
            }
            // 以下代码在synchronized同步块外面是安全的
            future.sync();

            if (!isRuning()) {
                log.error("等待连接服务器");
                ThreadUtils.waitFor(startedLock);
            }
        } catch (Exception t) {
            ThreadUtils.notifyWait(startedLock);
            throw t;
        }
    }

    class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            log.error("连接服务器成功");
            setRuning(true);
            NettyClient.this.socket = new Socket(ctx);
            ;
            ThreadUtils.notifyWait(startedLock);
            ctx.fireChannelActive();

            if (getConnectMonitor() != null) {
                getConnectMonitor().onConnected();
            }
        }

        // 链路关闭
        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            closeSocket();
            log.error("socket colse");
            if (getConnectMonitor() != null) {
                getConnectMonitor().onDisConnected();
            }
        }

        // 服务器 有数据过来了
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg.equals(NettyClient.HEAT_MSG_STRING)) { // 这是客户端的心跳包，不予理睬
                return;
            }
            onMessage(msg, ctx);
            ReferenceCountUtil.release(msg);
        }
    }

    private void closeSocket() {
        setRuning(false);
        if (socket != null) {
            socket.close();
        }
    }

    public void sendMessage(String msg) {
        if (msg == null) {
            return;
        }
        if (!isRuning() || socket == null) {
            throw new RuntimeException("链路已经被关闭，无法发送数据");
        }
        socket.writeMessage(msg);
    }

    private void setRuning(boolean isRuning) {
        lock.lock();
        try {
            this.isRuning = isRuning;
        } finally {
            lock.unlock();
        }
        this.isRuning = isRuning;
    }

    private boolean isRuning() {
        lock.lock();
        try {
            boolean t = this.isRuning;
            return t;
        } finally {
            lock.unlock();
        }
    }
}
