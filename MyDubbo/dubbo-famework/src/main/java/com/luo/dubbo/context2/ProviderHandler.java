package com.luo.dubbo.context2;

import java.util.HashMap;
import java.util.Map;

import com.luo.dubbo.registry.Bundle;
import com.luo.dubbo.registry.ComplexConnector;
import com.luo.dubbo.registry.Node;
import com.luo.dubbo.registry.ProviderNode;
import com.luo.dubbo.rpc.transport.ConnectMonitor;
import com.luo.dubbo.schema.bean.ServiceBean;
import com.luo.dubbo.util.Log;
import com.luo.dubbo.util.ReflectUtils;
import com.luo.dubbo.util.ThreadUtils;

public class ProviderHandler extends Handler {
    private Thread nettyThread = null;
    private final Object lock = new Object();

    public ProviderHandler(DubboContext context) {
        super(context);
    }

    public void handle() {
        // TODO Auto-generated method stub
        // 1. 获取本地配置 的 Provider
        Map<String, ServiceBean> services = ContextFactory.dubbo().getBeansOfType(ServiceBean.class);
        if (services.size() == 0) {
            Log.i("本地没有配置提供者");
            return;
        }
        // 启动 rpc监听
        rpcListen(getProxysInSpring(services));
        // 将提供者信息注入 到zk
        try {
            toRegistry(services);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /***
     * 在Spring中 找到代理对象
     * 
     * @param services
     * @return
     * @author HadLuo 2017年12月19日 新建
     */
    private Map<Class<?>, Object> getProxysInSpring(Map<String, ServiceBean> services) {
        Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
        // 向 注册中心注册 提供者
        ServiceBean serviceBean;
        Class<?> proxyClazz;
        Object proxy;
        for (String name : services.keySet()) {
            serviceBean = services.get(name);
            if ((proxyClazz = ReflectUtils.forClass(serviceBean.getInter())) == null) {
                throw new RuntimeException("不能加载提供者类 : " + serviceBean.getInter());
            }
            if ((proxy = ContextFactory.spring().getBean(serviceBean.getRef())) == null) {
                throw new RuntimeException("不能在spring中找到提供者实例对象: spring id:" + serviceBean.getRef());
            }
            map.put(proxyClazz, proxy);
        }
        return map;
    }

    /***
     * 监听 dubbo提供者接口
     * 
     * @param port
     * @param proxys
     * @author HadLuo 2017年12月1日 新建
     */
    private void rpcListen(final Map<Class<?>, Object> proxys) {
        if (nettyThread != null) {
            return;
        }
        nettyThread = new Thread() {
            public void run() {
                final long time = System.currentTimeMillis();
                System.err.println("start netty...");
                context().rpc().getServer(context().localIp, context().localPort, proxys,
                        new ConnectMonitor() {
                            public void onDisConnected() {
                            }

                            public void onConnected() {
                                System.err.println("started netty listen on " + context().localIp + " : "
                                        + context().localPort +"   耗时：" + (System.currentTimeMillis()-time) +" ms");
                                ThreadUtils.notifyWait(lock);
                            }
                        });
            };
        };
        nettyThread.start();
        ThreadUtils.waitFor(lock);
    }

    /**
     * 将提供者 注册 到 注册中心
     * 
     * @param services
     * @param registryBean
     * @param connector
     * @author HadLuo 2017年12月1日 新建
     */
    private void toRegistry(Map<String, ServiceBean> services) {
        // 向 注册中心注册 提供者
        ServiceBean serviceBean;
        for (String name : services.keySet()) {
            serviceBean = services.get(name);
            Node node = new ProviderNode();
            node.setInter(serviceBean.getInter());
            node.setAddress(context().localIp + ":" + context().localPort);
            node.setBundle(new Bundle());
            ((ComplexConnector) context().connector()).registerProvider(node);
        }
    }

    public void remove(Node node) {
        // TODO Auto-generated method stub
        
    }

    public void register(Node node) {
        // TODO Auto-generated method stub
        
    }
}
