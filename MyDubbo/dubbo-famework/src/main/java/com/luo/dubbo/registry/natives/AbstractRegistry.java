package com.luo.dubbo.registry.natives;

import com.luo.dubbo.exception.ConnectionException;
import com.luo.dubbo.registry.NodeWatcher;
import com.luo.dubbo.util.ThreadUtils;

public abstract class AbstractRegistry implements Registry {

    private Object lock = new Object();

    /***
     * dubbo内部调用
     * 
     * @param protocol
     * @author HadLuo 2017年11月17日 新建
     */
    public final void blockConnect(String protocol, NodeWatcher watcher) {
        connect(protocol, watcher);
        ThreadUtils.waitFor(lock);
    }

    /***
     * 客户端 连接成功 回调 打断阻塞
     * 
     * @author HadLuo 2017年11月17日 新建
     */
    public final void onSuceess() {
        ThreadUtils.notifyWait(lock);
    }

    /***
     * 客户端连接错误
     * 
     * @param e
     * @author HadLuo 2017年11月17日 新建
     */
    public final void onError(Throwable e) {
        throw new ConnectionException("连接zookeeper失败, cause:" + e.getMessage());
    }
}
