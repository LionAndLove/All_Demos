package com.luo.dubbo.rpc.transport;

import io.netty.channel.ChannelHandlerContext;

public class Socket {

    private ChannelHandlerContext context;

    public Socket(ChannelHandlerContext context) {
        this.context = context;
    }

    public void writeMessage(String msg) {
        if (null == msg || msg.isEmpty()) {
            return;
        }
        context.writeAndFlush(msg + NettyBootstrap.NEW_LINE);
    }

    public void close() {
        context.close();
        context = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        Socket other = (Socket) obj;
        return context == other.context;
    }

}
