package com.luo.dubbo.registry;

import com.luo.dubbo.registry.clusters.Server;

/***
 * 节点机器的 额外信息
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月19日 新建
 */
public class Bundle implements validator {
    /** xx */
    private static final long serialVersionUID = 1689480346463169588L;
    /***
     * 机器硬件信息
     */
    private Server hardware;

    public Bundle() {
        hardware = new Server();
    }

    public Server getHardware() {
        return hardware;
    }

    public void setHardware(Server hardware) {
        this.hardware = hardware;
    }

    public boolean validate() {
        return getHardware().validate();
    }

    @Override
    public String toString() {
        return "Bundle [hardware=" + hardware + "]";
    }
}
