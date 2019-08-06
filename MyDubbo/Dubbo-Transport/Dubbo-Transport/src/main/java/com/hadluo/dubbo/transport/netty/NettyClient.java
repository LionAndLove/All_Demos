package com.hadluo.dubbo.transport.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.hadluo.dubbo.common.NetUtils;
import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.ChannelHandler;
import com.hadluo.dubbo.transport.Client;
import com.hadluo.dubbo.transport.RemotingException;
import com.hadluo.dubbo.transport.wrapper.NettyChannel;
import com.hadluo.dubbo.transport.wrapper.NettyHandler;

/***
 * netty4 客户端 连接实现
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月9日 新建
 */
public class NettyClient extends AbstractEndpoint implements Client {

    private final Lock connectLock = new ReentrantLock();

    private Bootstrap bootstrap;

    public NettyClient(final URL url, final ChannelHandler handler) throws RemotingException {
        super(url, handler);
        try {
            doOpen();
        } catch (Throwable t) {
            close();
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to start "
                    + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                    + " connect to the server " + url.toInetSocketAddress() + ", cause: " + t.getMessage(), t);
        }
        try {
            connect();
            System.err.println("Start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                    + " connect to the server " + url.toInetSocketAddress());
        } catch (RemotingException t) {
            t.printStackTrace();
            close();
        } catch (Throwable t) {
            close();
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to start "
                    + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                    + " connect to the server " + url.toInetSocketAddress() + ", cause: " + t.getMessage(), t);
        }
    }

    private void connect() throws Throwable {
        connectLock.lock();
        try {
            ChannelFuture future = bootstrap.connect(url.getHost(), url.getPort());
            // 等待连接超时时间
            boolean ret = future.awaitUninterruptibly(3000, TimeUnit.MILLISECONDS);
            if (ret && future.isSuccess()) {
                io.netty.channel.Channel newChannel = future.channel();
                // 关闭旧的连接
                io.netty.channel.Channel oldChannel = getNettyChannel();
                if (oldChannel != null) {
                    try {
                        oldChannel.close();
                    } finally {
                        NettyChannel.removeChannelIfDisconnected(oldChannel);
                    }
                }
                // 复制 netty通道
                setNettyChannel(newChannel);
            } else if (future.cause() != null) {
                throw new RemotingException(this, "client(url: " + url + ") failed to connect to server "
                        + url.toInetSocketAddress() + ", error message is:" + future.cause().getMessage(),
                        future.cause());
            }
        } finally {
            connectLock.unlock();
        }
    }

    /***
     * netty4 客户端 实现
     * 
     * @author HadLuo 2018年5月9日 新建
     */
    private void doOpen() throws Throwable {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO));
        bootstrap.handler(new ChannelInitializer<io.netty.channel.Channel>() {
            @Override
            protected void initChannel(io.netty.channel.Channel ch) throws Exception {
                NettyCodecAdapter adapter = new NettyCodecAdapter(url);
                ch.pipeline().addLast(adapter.getDelimiterBasedFrameDecoder());
                ch.pipeline().addLast("decoder", adapter.getDecoder());
                ch.pipeline().addLast("encoder", adapter.getEncoder());
                ch.pipeline().addLast(new NettyHandler(handler, url));
            }
        });
    }

    @Override
    public boolean isConnected() {
        return channel().isConnected();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        if (getNettyChannel() == null) {
            return url.toInetSocketAddress();
        } else {
            return (InetSocketAddress) getNettyChannel().remoteAddress();
        }
    }

    @Override
    public void send(Object message) {
        channel().send(message);
    }

    @Override
    public Object getAttribute(String key) {
        return channel().getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        channel().setAttribute(key, value);
    }

    @Override
    public void close() {
        Channel channel = channel();
        if (channel != null) {
            channel.close();
        }
        if (getNettyChannel() != null) {
            getNettyChannel().close();
        }
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        Channel channel = channel();
        if (channel == null) {
            return null;
        }
        return channel.getLocalAddress();
    }

    @Override
    public void reconnect() {
        disconnect();
        try {
            connect();
            System.err.println("Start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                    + " connect to the server " + getRemoteAddress());
        } catch (RemotingException t) {
            t.printStackTrace();
            close();
        } catch (Throwable t) {
            close();
            t.printStackTrace();
        }
    }

    public void disconnect() {
        connectLock.lock();
        try {
            getNettyChannel().close();
            channel().close();
        } finally {
            connectLock.unlock();
        }
    }

    @Override
    public URL getUrl() {
        return url;
    }

}
