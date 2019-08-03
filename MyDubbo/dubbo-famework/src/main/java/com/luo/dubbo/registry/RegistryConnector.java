package com.luo.dubbo.registry;

import java.util.List;

import com.luo.dubbo.context2.Watcher;
import com.luo.dubbo.util.Assert;

/***
 * dubbo 与注册中心的链接器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月16日 新建
 */
public class RegistryConnector extends ComplexConnector {
    private final UnSafe unsafe;

    public RegistryConnector(Watcher watcher) {
        super();
        Assert.isNull(watcher, "节点监听器不能为空");
        unsafe = new UnSafe(registry(), watcher);
        setWatcher(unsafe.getWatcher0());
    }

    public List<Node> getProviderList() {
        // TODO Auto-generated method stub
        return unsafe.getProviderList();
    }

    public void registerProvider(Node nodeValue) {
        // TODO Auto-generated method stub
        unsafe.registerProvider(nodeValue);
    }

    public List<Node> getProviderList(List<String> interfaceList) {
        return unsafe.getProviderList(interfaceList);
    }
}
