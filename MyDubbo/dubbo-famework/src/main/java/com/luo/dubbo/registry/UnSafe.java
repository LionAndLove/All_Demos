package com.luo.dubbo.registry;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;
import com.luo.dubbo.context2.Watcher;
import com.luo.dubbo.registry.Node.PathInfo;
import com.luo.dubbo.registry.clusters.ClustersSelectStrategy;
import com.luo.dubbo.registry.clusters.DefaultStrategy;
import com.luo.dubbo.registry.natives.Registry;
import com.luo.dubbo.util.Assert;
import com.luo.dubbo.util.JsonUtils;
import com.luo.dubbo.util.Log;

/***
 * 与 register 通信桥梁
 * 
 * provider路径: /dubbo/providers/com.luo.IUser_127.0.0.1:2181 ==>
 * 节点值:{json{nodevalue}}
 * 
 * consumers路径: 占无
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月17日 新建
 */
public final class UnSafe implements Service {
    private static Logger log = Logger.getLogger(UnSafe.class);

    private Registry registry;

    private Watcher watcher;

    // 提供者的 负载均衡选择策略
    private final ClustersSelectStrategy strategy = new DefaultStrategy();

    private NodeWatcher watcher0 = new NodeWatcher() {

        public void remove(String nodePath) {
            Node node = Node.generate(nodePath,
                    JsonUtils.fromJson(new String(registry.getNodeValue(nodePath)), Bundle.class));
            if (node == null) {
                Log.e("注册中心节点移除事件，但是Node Path不能被解析   path:" + nodePath);
                return;
            }
            Log.e("注册中心节点移除事件， " + node.toString());
            watcher.remove(node);
        }

        public void register(String nodePath) {
            Node node = Node.generate(nodePath,
                    JsonUtils.fromJson(new String(registry.getNodeValue(nodePath)), Bundle.class));
            if (node == null) {
                Log.e("注册中心节点注册事件，但是Node Path不能被解析   path:" + nodePath);
                return;
            }
            Log.e("注册中心节点注册事件， " + node.toString());
            watcher.register(node);
        }
    };

    protected UnSafe(Registry registry, Watcher watcher) {
        this.registry = registry;
        this.watcher = watcher;
        Assert.isNull(registry, "registry is null");
    }

    /***
     * 获取所有的 提供者 节点
     */
    public List<Node> getProviderList() {
        List<Node> providers = get0(PathInfo.Root + PathInfo.P_PathLevel1, new ArrayList<String>(0));
        if (CollectionUtils.isEmpty(providers)) {
            log.error("providers 列表为空");
        }
        return providers;
    }

    public List<Node> getProviderList(List<String> interfaceList) {
        if (interfaceList == null) {
            interfaceList = new ArrayList<String>(0);
        }
        List<Node> providers = get0(PathInfo.Root + PathInfo.P_PathLevel1, interfaceList);
        if (CollectionUtils.isEmpty(providers)) {
            log.error("providers 列表为空");
        }
        return providers;
    }

    /***
     * 获取指定节点，指定 interfaces 的 节点
     * 
     * @param nodePath
     * @param interFilters
     * @return
     * @author HadLuo 2017年12月8日 新建
     */
    private List<Node> get0(String nodePath, List<String> interFilters) {
        List<Node> beans = new ArrayList<Node>();
        // 获取 所有的临时节点
        List<String> services = registry.childrenNodes(nodePath);
        Node bestNode = null;
        if (CollectionUtils.isEmpty(services)) {
            System.err.println("zookeeper 提供者节点为空");
            return beans;
        }
        for (String service : services) {
            Node node = Node.generate(nodePath + "/"+ service,
                    JsonUtils.fromJson(new String(registry.getNodeValue(nodePath + "/" + service)), Bundle.class));
            boolean flag = false;
            for (String i : interFilters) {
                if (i.equals(node.getInter())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                continue;
            }
            if (bestNode == null) {
                bestNode = node;
            } else {
                // 负载均衡选择
                boolean ret = strategy.select(bestNode.getBundle().getHardware(), node.getBundle()
                        .getHardware());
                bestNode = (ret ? bestNode : node);
            }
        }
        if (bestNode == null) {
            throw new RuntimeException("选择最优节点为空");
        }
        beans.add(bestNode);
        return beans;
    }

    public void registerProvider(Node node) {
        if (!node.validate()) {
            throw new IllegalArgumentException();
        }
        // ------ 这里不能 直接创建多级目录，否则zookeeper报错
        if (!registry.isExistence(PathInfo.Root)) {
            // 必须是 永久性节点信息， 因为 zk
            registry.createPersistentNode(PathInfo.Root);
        }
        String nodePath = node.wrap();
        if (!registry.isExistence(nodePath)) { // 不存在，插入
            registry.createTemporaryNode(nodePath, JsonUtils.toJson(node.getBundle()));
        } else { // 更新这个节点
            registry.setNode(nodePath, JsonUtils.toJson(node.getBundle()));
        }
    }

    public NodeWatcher getWatcher0() {
        return watcher0;
    }
}
