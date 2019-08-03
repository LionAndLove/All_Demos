package com.luo.dubbo.registry;

import java.util.List;

public interface Service {

    /**
     * 获取Provider 列表
     * 
     * @return
     * @author HadLuo 2017年11月17日 新建
     */
    public List<Node> getProviderList();

    /***
     * 获取 指定的 interface 路径的 提供者节点
     * 
     * @param interfaceList
     * @return
     * @author HadLuo 2017年12月8日 新建
     */
    public List<Node> getProviderList(List<String> interfaceList);

    /***
     * 注册一个提供者
     * 
     * @param nodeValue
     * @author HadLuo 2017年11月17日 新建
     */
    public void registerProvider(Node nodeValue);
}
