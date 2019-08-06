package com.hadluo.dubbo.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetSocketAddress;
import java.util.Collection;
import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.ChannelHandler;
import com.hadluo.dubbo.transport.RemotingException;
import com.hadluo.dubbo.transport.Server;
import com.hadluo.dubbo.transport.wrapper.NettyChannel;
import com.hadluo.dubbo.transport.wrapper.NettyHandler;

/***
 * netty4 服务端 实现 (注意:dubbo内部使用netty3)
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class NettyServer extends AbstractEndpoint implements Server {

    /***
     * 绑定的 地址
     */
    private InetSocketAddress bindAddress;
    /** 最大连接数 */
    private int accepts;

    public NettyServer(final URL url, final ChannelHandler handler) throws RemotingException {
        super(url, handler);
        bindAddress = new InetSocketAddress(url.getHost(), url.getPort());
        accepts = url.getParameter(Constants.ACCEPTS_KEY, Constants.DEFAULT_ACCEPTS);
        try {
            doOpen();
            System.err.println("Start " + getClass().getSimpleName() + " bind " + getLocalAddress()
                    + ", export " + getLocalAddress());
        } catch (Throwable t) {
            throw new RemotingException(url.toInetSocketAddress(), null, "Failed to bind "
                    + getClass().getSimpleName() + " on " + getLocalAddress() + ", cause: " + t.getMessage(),
                    t);
        }
    }

    /***
     * netty4 绑定服务端实现,与dubbo使用的netty3有所区别!!!
     * 
     * @throws Throwable
     * @author HadLuo 2018年5月8日 新建
     */
    private void doOpen() throws Throwable {
        // boss线程数量1个 （处理客户端连接的线程）
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // worker线程数量 按cpu核心算（处理读写）
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.ERROR))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // 有socket连接了 会回调
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // dubbo的 编码太复杂了，这里不坐解析，我们直接使用string作为编解码
                        NettyCodecAdapter adapter = new NettyCodecAdapter(url);
                        // 指定粘包处理
                        ch.pipeline().addLast(adapter.getDelimiterBasedFrameDecoder());
                        ch.pipeline().addLast("decoder", adapter.getDecoder());
                        ch.pipeline().addLast("encoder", adapter.getEncoder());
                        ch.pipeline().addLast(new NettyHandler(handler, url));
                    };

                }).option(ChannelOption.SO_BACKLOG, accepts).childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture future = serverBootstrap.bind(url.getPort());
        // 设置 channel
        setNettyChannel(future.channel());
        future.sync();
        future.channel().closeFuture().sync();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return bindAddress;
    }

    @Override
    public void close() {
        // 关闭 server绑定的 通道
        if (getNettyChannel() != null) {
            // unbind.
            getNettyChannel().close();
        }
        try {
            // 关闭每个客户端连接的通道
            Collection<NettyChannel> channels = NettyChannel.channels();
            if (channels != null && channels.size() > 0) {
                for (NettyChannel channel : channels) {
                    channel.close();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (NettyChannel.channels() != null) {
            NettyChannel.channels().clear();
        }
    }

    @Override
    public URL getUrl() {
        // TODO Auto-generated method stub
        return url;
    }
}
