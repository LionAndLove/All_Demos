package com.luo.dubbo.rpc.transport;

/***
 * 连接监听
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月19日 新建
 */
public interface ConnectMonitor {

    /***
     * 绑定 成功
     * 
     * @author HadLuo 2017年12月19日 新建
     */
    public void onConnected();

    /***
     * 解绑 断开连接
     * 
     * @param exception
     * @author HadLuo 2017年12月19日 新建
     */
    public void onDisConnected();

}
