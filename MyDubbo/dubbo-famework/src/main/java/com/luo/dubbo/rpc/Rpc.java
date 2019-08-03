package com.luo.dubbo.rpc;

import java.util.Map;

import com.luo.dubbo.rpc.transport.ConnectMonitor;

public interface Rpc {

    /***
     * 服务端: 绑定远程接口
     * 
     * @param proxysOptions : key:父接口的class  value:实现类对象
     * @param host
     * @param port
     * @author HadLuo 2017年11月20日 新建
     */
    public void getServer(String host, int port, Map<Class<?>, Object> proxysOptions,ConnectMonitor connectMonitor);

    /***
     * 客户端： 获取 某个 接口的 远程代理对象
     * 
     * @param inter
     * @param protocol
     * @return
     * @author HadLuo 2017年11月17日 新建
     */
    public <T> T getProxy(Class<T> inter, String host, int port, ConnectMonitor connectMonitor);
}
