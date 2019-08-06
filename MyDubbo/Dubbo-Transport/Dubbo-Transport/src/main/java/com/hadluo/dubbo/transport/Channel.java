package com.hadluo.dubbo.transport;

import java.net.InetSocketAddress;

/***
 * netty channel的包装器 (代表一个socket通道)
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public interface Channel extends Endpoint {

    /**
     * is connected.
     * 
     * @return connected
     */
    boolean isConnected();

    /**
     * 获取 远端地址
     * 
     * @return remote address.
     */
    InetSocketAddress getRemoteAddress();

    /***
     * 发送数据
     * 
     * @param message
     * @author HadLuo 2018年5月8日 新建
     */
    void send(Object message);

    /**
     * 获取 通道 的属性值
     * 
     * @param key key.
     * @return value.
     */
    Object getAttribute(String key);

    /**
     * 设置通道的属性值
     * 
     * @param key key.
     * @param value value.
     */
    void setAttribute(String key, Object value);
}
