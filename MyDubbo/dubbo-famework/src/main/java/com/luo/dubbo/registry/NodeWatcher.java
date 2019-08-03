package com.luo.dubbo.registry;

/***
 * 节点监听器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月1日 新建
 */
public interface NodeWatcher {

    /***
     * 节点被 移除了
     * 
     * @param nodeValue
     * @author HadLuo 2017年12月1日 新建
     */
    public void remove(String nodeValue);

    /***
     * 节点 被 注册了
     * 
     * @param nodeValue
     * @author HadLuo 2017年12月1日 新建
     */
    public void register(String nodeValue);
}
