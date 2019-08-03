package com.luo.dubbo.registry.natives;

import java.util.List;

import com.luo.dubbo.exception.ConnectionException;
import com.luo.dubbo.registry.NodeWatcher;

/***
 * 注册中心
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月15日 新建
 */
public interface Registry {
    public void connect(String protocol, NodeWatcher watcher) throws ConnectionException;
    public void disconnect();
    /***
     * 创建临时节点信息
     * 
     * @author HadLuo 2017年11月16日 新建
     */
    public void createTemporaryNode(String node , String value);
    /**创建临时节点信息*/
    public void createTemporaryNode(String node);
    
    /**创建永久节点信息*/
    public void createPersistentNode(String node , String value);
    /**创建永久节点信息*/
    public void createPersistentNode(String node);
    
    /***
     * 修改节点的 值
     * @param path
     * @param data
     * @author HadLuo  2017年11月17日 新建
     */
    public void setNode(String node,String value);

    /***
     * 是否存在
     * 
     * @param key
     * @param value
     * @return
     * @author HadLuo 2017年11月16日 新建
     */
    public boolean isExistence(String node);

    /***
     * 获取所有的 子节点 列表
     * 
     * @param value
     * @return
     * @author HadLuo 2017年11月16日 新建
     */
    public List<String> childrenNodes(String parent);
    
    /***
     * 获取子节点 的第一个节点
     * @param parent
     * @return
     * @author HadLuo  2017年11月16日 新建
     */
    public String childrenNode(String parent) ;
    
    /***
     * 获取 对应node 的值 
     * @param node
     * @return
     * @author HadLuo  2017年11月16日 新建
     */
    public byte[] getNodeValue(String node);
}
