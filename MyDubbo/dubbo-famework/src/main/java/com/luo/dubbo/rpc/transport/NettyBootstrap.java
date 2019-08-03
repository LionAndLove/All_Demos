package com.luo.dubbo.rpc.transport;

import io.netty.channel.ChannelHandlerContext;

public abstract class NettyBootstrap {

    /***
     * 指定字符分隔
     */
    public static final String NEW_LINE = "#$%!";
    /***
     * 心跳字符
     */
    public static final String HEAT_MSG_STRING = "@*!@" + NEW_LINE;
    public static final String HEAT_MSG_STRING2 = "@*!@";

    public static final int hear_time = 10;

    @SuppressWarnings("rawtypes")
    private MessageListener messageListener;

    private ConnectMonitor connectMonitor;

    public NettyBootstrap(@SuppressWarnings("rawtypes") MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    @SuppressWarnings("unchecked")
    public void onMessage(Object msg, ChannelHandlerContext remoteSocket) {
        if (messageListener != null) {
            messageListener.onMessage(msg, new Socket(remoteSocket));
        }
    }

    /***
     * 服务器绑定初始化
     * 
     * @param host
     * @param port
     * @throws Exception
     * @author HadLuo 2017年11月21日 新建
     */
    public void bind(String host, int port, ConnectMonitor connectMonitor) throws Exception {
        // TODO
    }

    /***
     * 客户端 连接 服务器
     * 
     * @param host
     * @param port
     * @throws Exception
     * @author HadLuo 2017年11月21日 新建
     */
    public void connect(String host, int port, ConnectMonitor connectMonitor) throws Exception {
        // TODO
    }

    public void setConnectMonitor(ConnectMonitor connectMonitor) {
        this.connectMonitor = connectMonitor;
    }

    public ConnectMonitor getConnectMonitor() {
        return connectMonitor;
    }
}
