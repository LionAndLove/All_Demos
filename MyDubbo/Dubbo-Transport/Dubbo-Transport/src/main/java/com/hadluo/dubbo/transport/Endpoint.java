package com.hadluo.dubbo.transport;

import java.net.InetSocketAddress;

import com.hadluo.dubbo.common.URL;


/***
 * 代表 一个 netty端（server or client）
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public interface Endpoint {
    
    /**
     * get url.
     * 
     * @return url
     */
    URL getUrl();
    /**
     * 关闭 channel通道
     */
    void close();

    /**
     * get local address.
     * 
     * @return local address.
     */
    InetSocketAddress getLocalAddress();
}