package com.hadluo.dubbo.transport.wrapper;

import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.ChannelHandler;

public abstract class HeaderExchangeHandlerAdapter implements ChannelHandler {

    @Override
    public void connected(Channel channel) {
    }

    @Override
    public void disconnected(Channel channel) {
        // TODO Auto-generated method stub
    }

    @Override
    public void caught(Channel channel, Throwable exception) {
        // TODO Auto-generated method stub
    }

    /***
     * 收到 远端 数据
     */
    public abstract void received(Channel channel, Object message);
}
