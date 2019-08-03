package com.luo.dubbo.rpc.transport;

public interface MessageListener<T> {

    /***
     * 对端有消息过来
     * 
     * @param msg 远端的消息
     * @param remoteSocket 远端的socket
     * @author HadLuo 2017年11月21日 新建
     */
    public void onMessage(T msg , Socket remoteSocket);
}
