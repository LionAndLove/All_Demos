package com.hadluo.dubbo.transport;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/***
 * 封装多个 ChannelHandler ， netty事件 回调时 ，会回调执行每一个注册过的 ChannelHandler
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class ChannelHandlerDispatcher implements ChannelHandler {
    private final Collection<ChannelHandler> channelHandlers = new CopyOnWriteArraySet<ChannelHandler>();

    public ChannelHandlerDispatcher(ChannelHandler... handlers) {
        this.channelHandlers.addAll(Arrays.asList(handlers));
    }

    /***
     * 添加 一个 channelHandler 到 集合
     * 
     * @param channelHandler
     * @return
     * @author HadLuo 2018年5月8日 新建
     */
    public ChannelHandler add(ChannelHandler channelHandler) {
        channelHandlers.add(channelHandler);
        return this;
    }

    @Override
    public void connected(Channel channel) {
        for (ChannelHandler listener : channelHandlers) {
            listener.connected(channel);
        }
    }

    @Override
    public void disconnected(Channel channel) {
        for (ChannelHandler listener : channelHandlers) {
            listener.disconnected(channel);
        }
    }

    @Override
    public void received(Channel channel, Object message) {
        for (ChannelHandler listener : channelHandlers) {
            listener.received(channel, message);
        }
    }

    @Override
    public void caught(Channel channel, Throwable exception) {
        for (ChannelHandler listener : channelHandlers) {
            listener.caught(channel, exception);
        }
    }

}
