package com.luo.dubbo.registry;

import org.apache.log4j.Logger;
import com.luo.dubbo.context2.ContextFactory;
import com.luo.dubbo.registry.natives.AbstractRegistry;
import com.luo.dubbo.registry.natives.Registry;
import com.luo.dubbo.registry.natives.RegistryFactory;
import com.luo.dubbo.schema.bean.RegistryBean;
import com.luo.dubbo.util.Assert;

public abstract class ComplexConnector implements Connector, Service {
    private static Logger log = Logger.getLogger(ComplexConnector.class);
    private volatile Registry registry;
    /** <dubbo:registry> 标签 信息 bean */
    private RegistryBean registryBean;
    private NodeWatcher watcher;

    public ComplexConnector() {
        // 根据客户端 设置 的 注册中心 实例化 具体 注册中心 对象
        registryBean = ContextFactory.spring().getBean(RegistryBean.class);
        Assert.isNull(registryBean, "不能获取<dubbo:registry> 标签 对象实例");
    }

    public void setWatcher(NodeWatcher watcher) {
        this.watcher = watcher;
    }

    public void connectRegistry() {
        log.info("开始与注册中心建立连接  address>>" + registryBean.getAddress());
        long t = System.currentTimeMillis();
        ((AbstractRegistry) registry()).blockConnect(registryBean.getAddress(), watcher);
        log.info("与注册中心 " + registryBean.getAddress() + " 连接成功  ， 耗时: " + (System.currentTimeMillis() - t)
                + "ms");
    }

    public void disconnect() {
        registry().disconnect();
    }

    /***
     * 获取registry对象
     * 
     * @return
     * @author HadLuo 2017年11月17日 新建
     */
    Registry registry() {
        if (null == registry) {
            registry = RegistryFactory.createRegistry(registryBean.getImplClass());
        }
        return registry;
    }
}
