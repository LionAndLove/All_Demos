package com.luo.dubbo.registry.clusters;

/***
 * 默认的选择策略
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月8日 新建
 */
public class DefaultStrategy implements ClustersSelectStrategy {

    /***
     * 优先比较剩余内存,然后在比较剩余cpu
     * 
     * @param nodeValue
     * @return true :选择server1 false 选择server2
     * @author HadLuo 2017年12月8日 新建
     */
    public boolean select(Server server1, Server server2) {

        if (server1.getMemory() == server2.getMemory()) {
            if (server1.getCpu() > server2.getCpu()) {
                return true;
            } else {
                return false;
            }
        }

        if (server1.getMemory() > server2.getMemory()) {
            return true;
        }
        return false;
    }

}
