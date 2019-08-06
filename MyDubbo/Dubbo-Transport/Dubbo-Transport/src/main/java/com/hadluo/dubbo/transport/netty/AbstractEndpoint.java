package com.hadluo.dubbo.transport.netty;

import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.ChannelHandler;
import com.hadluo.dubbo.transport.Endpoint;
import com.hadluo.dubbo.transport.wrapper.NettyChannel;

public abstract class AbstractEndpoint implements Endpoint {

    volatile io.netty.channel.Channel nettyChannel;

    /** dubbo url */
    final URL url;
    /** handler 链 */
    final ChannelHandler handler;

    public AbstractEndpoint(final URL url, final ChannelHandler handler) {
        this.url = url;
        this.handler = handler;
    }

    protected Channel channel() {
        io.netty.channel.Channel ch = nettyChannel;
        if (ch == null) {
            return null;
        }
        return NettyChannel.getOrAddChannel(ch, url);
    }

    public io.netty.channel.Channel getNettyChannel() {
        return nettyChannel;
    }

    public void setNettyChannel(io.netty.channel.Channel nettyChannel) {
        this.nettyChannel = nettyChannel;
    }
}
