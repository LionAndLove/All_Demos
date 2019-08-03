package com.luo.dubbo.registry;

import org.springframework.util.StringUtils;
import com.luo.dubbo.util.PatternUtils;

/***
 * 代表注册中心的 一个 动态 节点 : /com.luo.IUser/127.0.0.1:2181 <br>
 * 
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月8日 新建
 */
public abstract class Node implements validator {

    /** xx */
    private static final long serialVersionUID = -7602890759800777965L;

    public abstract static class PathInfo {
        public static final String Root = "/dubbo";
        public static final String P_PathLevel1 = "/providers";
        public static final String C_PathLevel1 = "/consumers";

        public static boolean isProvider(String node) {
            return node.startsWith(Root + P_PathLevel1);
        }

        public static boolean isConsumer(String node) {
            return node.startsWith(Root + C_PathLevel1);
        }
    }

    public static Node generate(String nodePath, Bundle bundle) {
        Node node = null;
        if (PathInfo.isProvider(nodePath)) {
            node = new ProviderNode();
            node.parser(nodePath, bundle);
        } else if (PathInfo.isConsumer(nodePath)) {
            node = new ConsumerNode();
            node.parser(nodePath, bundle);

        }
        return node;
    }

    /***
     * ip:port 127.0.0.1:8080
     */
    private String address;
    /***
     * 接口
     */
    private String inter;

    private Bundle bundle;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public boolean validate() {
        if (!PatternUtils.isJavaPackage(getInter())) {
            throw new IllegalArgumentException("interface 格式错误， interface:" + getInter());
        }
        if (!PatternUtils.isAddress(getAddress())) {
            throw new IllegalArgumentException("address 格式错误， address:" + getInter());
        }
        return getBundle().validate();
    }

    public String ip() {
        return getAddress().split(":")[0];
    }

    public int port() {
        return Integer.parseInt(getAddress().split(":")[1]);
    }

    /***
     * 将zk节点 解析 成Node
     * 
     * @param nodePath
     * @author HadLuo 2017年12月19日 新建
     */
    public void parser(String nodePath, Bundle val) {
        // 去掉头部
        nodePath = StringUtils.replace(nodePath, header(), "");
        if(nodePath.startsWith("/")){
            nodePath = nodePath.substring(1);
        }
        String[] infos = nodePath.split("_");
        if (infos == null || infos.length != 2) {
            throw new IllegalArgumentException("节点值格式错误 NodePath:" + nodePath);
        }
        if (!PatternUtils.isJavaPackage(infos[0])) {
            throw new IllegalArgumentException("节点值格式错误 NodePath:" + nodePath);
        }
        if (!PatternUtils.isAddress(infos[1])) {
            throw new IllegalArgumentException("节点值格式错误 NodePath:" + nodePath);
        }
        setInter(infos[0]);
        setAddress(infos[1]);
        setBundle(val);
    }

    /***
     * 将 node包装成 zk节点
     * 
     * @param node
     * @return
     * @author HadLuo 2017年12月20日 新建
     */
    public String wrap() {
        StringBuilder nodePath = new StringBuilder();
        if (this instanceof ProviderNode) {
            nodePath.append(PathInfo.Root).append(PathInfo.P_PathLevel1);
        } else {
            nodePath.append(PathInfo.Root).append(PathInfo.C_PathLevel1);
        }
        return nodePath.append("/").append(this.getInter()).append("_").append(this.getAddress()).toString();
    }

    /***
     * 获取头部 /dubbo/providers
     * 
     * @param header
     * @return
     * @author HadLuo 2017年12月19日 新建
     */
    public abstract String header();

    @Override
    public String toString() {
        return "Node [address=" + address + ", inter=" + inter + ", bundle=" + bundle + "]";
    }
}
