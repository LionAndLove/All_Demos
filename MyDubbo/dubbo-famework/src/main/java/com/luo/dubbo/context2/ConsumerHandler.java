package com.luo.dubbo.context2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.luo.dubbo.registry.ComplexConnector;
import com.luo.dubbo.registry.Node;
import com.luo.dubbo.rpc.transport.ConnectMonitor;
import com.luo.dubbo.schema.bean.ReferenceBean;
import com.luo.dubbo.schema.bean.ReferenceBeanProxy;
import com.luo.dubbo.util.Log;

public class ConsumerHandler extends Handler {

    public ConsumerHandler(DubboContext context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public void handle() {
        // 获取 本地 的 消费者
        Map<String, ReferenceBean> beans = ContextFactory.dubbo().getBeansOfType(ReferenceBean.class);
        if(beans.size() == 0){
            Log.i("本地配置的消费者为空");
            return ;
        }
        syncRegistry(beans);
    }

    public void syncRegistry(Map<String, ReferenceBean> beans) {
        // 获取注册中心 所有的 提供者
        List<Node> providers = ((ComplexConnector) context().connector())
                .getProviderList(assembleInterfacesPath(beans));
        Log.i("拉取到注册中心的提供者: " + providers);
        ReferenceBean referenceBean;
        for (String id : beans.keySet()) {
            boolean flag = false;
            referenceBean = beans.get(id);
            for (Node provider : providers) {
                // 找到了注册中心对应的 提供者
                if (provider.getInter().equals(referenceBean.getInter())) {
                    createProxy(provider, referenceBean);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                // 没有 这个 提供者 存在
                Log.e("提供者在注册中心无法找到，请检查是否正在运行， interface:" + referenceBean.getInter());
            }
        }
    }

    private List<String> assembleInterfacesPath(Map<String, ReferenceBean> beans) {
        List<String> interfaces = new ArrayList<String>();
        for (String id : beans.keySet()) {
            interfaces.add(beans.get(id).getInter());
        }
        return interfaces;
    }

    /***
     * 创建远程代理对象 ， 并注入到spring
     * 
     * @param val
     * @param referenceBean
     * @author HadLuo 2017年11月20日 新建
     */
    private void createProxy(Node provider, ReferenceBean referenceBean) {
        try {
            // 获取远程代理对象
            Log.i("开始连接提供者...   ip:" + provider.ip() + "   port:" + provider.port());
            Object proxy = context().rpc().getProxy(Class.forName(provider.getInter()), provider.ip(),
                    provider.port(), new ConnectMonitor() {
                        public void onDisConnected() {
                            // TODO Auto-generated method stub
                        }

                        public void onConnected() {
                            // TODO Auto-generated method stub
                        }
                    });
            if (proxy == null) {
                Log.e("不能创建远程代理对象 ，address:" + provider.getAddress() + "  interface:" + provider.getInter());
                return;
            }
            injectSpring(proxy, referenceBean);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("不能创建远程代理对象 ，address:" + provider.getAddress() + "  interface:" + provider.getInter());
        }
    }

    /***
     * 将代理对象 注入到 spring容器
     * 
     * @param proxy
     * @param referenceBean
     * @author HadLuo 2017年12月8日 新建
     */
    private void injectSpring(Object proxy, ReferenceBean referenceBean) {
        // 把 这个对象 注入给spring
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("proxy", proxy);
        properties.put("id", referenceBean.getId());
        // 重新覆盖 ReferenceBean的值 ，带了 代理对象 ,
        // ReferenceBean内部采用工厂构建，直接指定对象就是proxy
        ContextFactory.spring().setBean(referenceBean.getId(), ReferenceBeanProxy.class, properties);
        Log.i("提供者代理对象注册成功   class:" + referenceBean.getInter());
    }

    public void remove(Node node) {
        // TODO Auto-generated method stub
        
    }

    public void register(Node node) {
        // TODO Auto-generated method stub
        
    }

}
