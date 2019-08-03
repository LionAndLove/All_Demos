package com.luo.dubbo.registry.clusters;

/***
 * 服务的 负载均衡选择策略
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月8日 新建
 */
public interface ClustersSelectStrategy {
    /***
     * 
     * @param nodeValue
     * @return true :选择server1 false 选择server2
     * @author HadLuo 2017年12月8日 新建
     */
    public boolean select(Server server1, Server server2);

}
