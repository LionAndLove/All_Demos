package com.luo.dubbo.rpc.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.luo.dubbo.util.Assert;

/***
 * 简单 字符串编解码 ，长连接 的 netty服务器 rpc专用
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月20日 新建
 */
public final class NettyServer extends NettyBootstrap {

    private final AcceptorIdleStateTrigger idleStateTrigger = new AcceptorIdleStateTrigger();

    public NettyServer(@SuppressWarnings("rawtypes") MessageListener messageListener) {
        super(messageListener);
        Assert.isNull(messageListener, "消息接收器不能为空");
    }

    public void bind(String host, int port, ConnectMonitor connectMonitor) throws Exception {
        setConnectMonitor(connectMonitor);
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.ERROR))
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /***
                             * 指定字符 解码器
                             */
                            ch.pipeline().addLast(
                                    new DelimiterBasedFrameDecoder(1024 * 1024 * 10, Unpooled
                                            .copiedBuffer(NEW_LINE.getBytes())));
                            /***
                             * 第一个参数： 多少秒 没有 读到数据 就 触发 idleStateTrigger 的
                             * userEventTriggered
                             */
                            ch.pipeline()
                                    .addLast(new IdleStateHandler(hear_time + 2, 0, 0, TimeUnit.SECONDS));
                            ch.pipeline().addLast(idleStateTrigger);
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encoder", new StringEncoder());
                            ch.pipeline().addLast(new ServerHandler());
                        };

                    }).option(ChannelOption.SO_BACKLOG, 65535).childOption(ChannelOption.SO_KEEPALIVE, true);
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = sbs.bind(port).sync();
            future.addListener(new GenericFutureListener<ChannelFuture>() {
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (getConnectMonitor() != null) {
                        getConnectMonitor().onConnected();
                    }
                }
            });
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            throw e;
        }
    }

    class ServerHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            // TODO Auto-generated method stub
            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg.equals(NettyClient.HEAT_MSG_STRING2)) { // 这是客户端的心跳包，不予理睬
                return;
            }
            // 客户端 有数据过来
            onMessage(msg, ctx);
            ReferenceCountUtil.release(msg);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
