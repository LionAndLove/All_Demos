package com.luo.dubbo.registry.natives;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.util.CollectionUtils;

import com.luo.dubbo.registry.NodeWatcher;

/***
 * 注册中心的 默认实现 zkClient
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月16日 新建
 */
public class ZKRegistry extends AbstractRegistry {

    private ZooKeeper zk;

    private static Logger log = Logger.getLogger(ZKRegistry.class);

    public void connect(String protocol, final NodeWatcher watcher) {
        // 创建一个与服务器的连接
        try {
            zk = new ZooKeeper(protocol, 6000, new Watcher() {
                // 监控所有被触发的事件
                public void process(WatchedEvent event) {
                    if (event.getState() == KeeperState.SyncConnected) { // 连接成功
                        onSuceess();
                        return;
                    }
                    if (event.getType() == EventType.NodeDeleted) { // 节点被删除了
                        watcher.remove(event.getPath());
                        return;
                    }
                    if (event.getType() == EventType.NodeCreated) { // 节点被创建了
                        watcher.register(event.getPath());
                        return;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            log.error("连接zookeeper失败", e.getCause());
            onError(e);
        }
    }

    public void createTemporaryNode(String node, String value) {
        /***
         * CreateMode.PERSISTENT 永久性节点 CreateMode.PERSISTENT_SEQUENTIAL 永久性序列节点
         * CreateMode.EPHEMERAL 临时节点，会话断开或过期时会删除此节点
         * CreateMode.PERSISTENT_SEQUENTIAL 临时序列节点，会话断开或过期时会删除此节点
         */
        try {
            zk.create(node, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper create操作失败");
        }
    }

    public void createTemporaryNode(String node) {
        try {
            zk.create(node, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper create操作失败");
        }
    }

    public void createPersistentNode(String node, String value) {
        /***
         * CreateMode.PERSISTENT 永久性节点 CreateMode.PERSISTENT_SEQUENTIAL 永久性序列节点
         * CreateMode.EPHEMERAL 临时节点，会话断开或过期时会删除此节点
         * CreateMode.PERSISTENT_SEQUENTIAL 临时序列节点，会话断开或过期时会删除此节点
         */
        try {
            zk.create(node, value.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper create操作失败");
        }
    }

    public void createPersistentNode(String node) {
        try {
            zk.create(node, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper create操作失败");
        }
    }

    public boolean isExistence(String node) {
        try {
            return zk.exists(node, true) != null ? true : false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper exists操作失败");
        }
    }

    public List<String> childrenNodes(String parent) {
        try {
            return zk.getChildren(parent, true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public String childrenNode(String parent) {
        try {
            List<String> subList = zk.getChildren(parent, true);
            if (CollectionUtils.isEmpty(subList)) {
                return "";
            }
            return subList.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public byte[] getNodeValue(String node) {
        try {
            return zk.getData(node, false, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper getData操作失败");
        }
    }

    public void disconnect() {
        try {
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper close操作失败");
        }
    }

    public void setNode(String node, String value) {
        try {
            zk.setData(node, value.getBytes(), -1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("zookeeper setData操作失败");
        }
    }

}
