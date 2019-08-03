package com.luo.dubbo.registry;


public interface Connector{

    /***
     * 连接注册中心
     * 
     * @author HadLuo  2017年11月17日 新建
     */
    public void connectRegistry();
    
    /***
     * 主动断开连接
     * 
     * @author HadLuo  2017年11月17日 新建
     */
    public void disconnect();
}
