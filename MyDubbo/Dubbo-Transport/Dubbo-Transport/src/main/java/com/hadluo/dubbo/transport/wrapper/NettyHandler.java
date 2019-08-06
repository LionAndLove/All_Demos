package com.hadluo.dubbo.transport.wrapper;

import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.ChannelHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/***
 * netty 事件处理 Handler的 包装器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class NettyHandler extends ChannelInboundHandlerAdapter {

    /** 客户端 ChannelHandler 链 （ChannelHandlerDispatcher） */
    private final ChannelHandler handler;

    private final URL url;

    public NettyHandler(ChannelHandler handler, URL url) {
        this.handler = handler;
        this.url = url;
    }

    /***
     * channel connected event
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url);
        try {
            if (handler != null && channel != null) {
                // 回调 客户端 ChannelHandler 链 connected事件
                handler.connected(channel);
            }
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /***
     * channel disconnected event
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url);
        try {
            if (handler != null && channel != null) {
                // 回调 客户端 ChannelHandler 链 disconnected 事件
                handler.disconnected(channel);
            }
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * channel message readable event
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url);
        try {
            if (handler != null && channel != null) {
                // 回调 客户端 ChannelHandler 链 received 事件
                handler.received(channel, msg);
            }
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * channel exception event
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url);
        try {
            if (handler != null && channel != null) {
                // 回调 客户端 ChannelHandler 链 caught 事件
                handler.caught(channel, cause);
            }
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

}
