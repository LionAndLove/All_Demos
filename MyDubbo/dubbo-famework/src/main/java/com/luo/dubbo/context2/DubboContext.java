package com.luo.dubbo.context2;

import java.util.concurrent.atomic.AtomicBoolean;

import com.luo.dubbo.registry.Connector;
import com.luo.dubbo.registry.Node;
import com.luo.dubbo.registry.ProviderNode;
import com.luo.dubbo.registry.RegistryConnector;
import com.luo.dubbo.rpc.NettyRpc;
import com.luo.dubbo.rpc.Rpc;
import com.luo.dubbo.schema.bean.ProtocolBean;
import com.luo.dubbo.util.NetworkUtils;

public class DubboContext extends AbstractDubboContext {

    String localIp;
    int localPort;
    private ProviderHandler providerHandler;
    private ConsumerHandler consumerHandler;

    volatile AtomicBoolean isOnCreate = new AtomicBoolean(false);
    /***
     * rpc
     */
    private Rpc rpc;
    /***
     * 注册中心连接器
     */
    private Connector connector;

    @Override
    public void onCreate() {
        isOnCreate.set(true);
        super.onCreate();
        rpc = new NettyRpc();
        providerHandler = new ProviderHandler(this);
        consumerHandler = new ConsumerHandler(this);
        connector = new RegistryConnector(new Watcher() {
            public void remove(Node node) {
                if (node instanceof ProviderNode) {
                    providerHandler.remove(node);
                } else {
                    consumerHandler.remove(node);
                }
            }

            public void register(Node node) {
                if (node instanceof ProviderNode) {
                    providerHandler.register(node);
                } else {
                    consumerHandler.register(node);
                }
            }
        });
        // 连接注册中心
        connector.connectRegistry();
        // // rpc监听信息
        ProtocolBean protocolBean = ContextFactory.spring().getBean(ProtocolBean.class);
        localPort = protocolBean.getPort();
        localIp = NetworkUtils.getLinuxLocalIp();

        handle();
    }

    public void handle() {
        consumerHandler.handle();
        providerHandler.handle();
    }

    /***
     * 获取netty rpc
     * 
     * @return
     * @author HadLuo 2017年12月14日 新建
     */
    Rpc rpc() {
        return rpc;
    }

    /***
     * 获取连接器
     * 
     * @return
     * @author HadLuo 2017年11月17日 新建
     */
    Connector connector() {
        return connector;
    }

    public boolean isOnCreate() {
        return isOnCreate.get();
    }

}
