package com.hadluo.dubbo.transport;

public interface Client extends Channel {
    /***
     * 心跳超时 时 断线重连
     * 
     * @author HadLuo 2018年5月8日 新建
     */
    void reconnect();
}
