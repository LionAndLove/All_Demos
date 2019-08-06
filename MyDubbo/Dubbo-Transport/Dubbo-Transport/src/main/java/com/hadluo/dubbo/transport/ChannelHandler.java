package com.hadluo.dubbo.transport;

/***
 * netty handler的包装器 （一些事件）
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public interface ChannelHandler {

    /***
     * 连接成功事件
     * 
     * @param channel
     * @author HadLuo 2018年5月8日 新建
     */
    void connected(Channel channel);

    /***
     * 断开连接事件
     * 
     * @param channel
     * @author HadLuo 2018年5月8日 新建
     */
    void disconnected(Channel channel);

    /**
     * 消息读事件
     * 
     * @param channel channel.
     * @param message message.
     */
    void received(Channel channel, Object message);

    /**
     * 异常事件
     * 
     * @param channel channel.
     * @param exception exception.
     */
    void caught(Channel channel, Throwable exception);

}