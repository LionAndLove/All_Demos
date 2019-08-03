package com.luo.dubbo.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyPool {
    /***
     * key: 父接口class name<br>
     * value: 实现类 实例
     */
    private final ConcurrentHashMap<String, Object> proxys = new ConcurrentHashMap<String, Object>();

    public void reset(Map<Class<?>, Object> info) {
        proxys.clear();
        for (Class<?> parentClass : info.keySet()) {
            proxys.put(parentClass.getName(), info.get(parentClass));
        }
    }

    public Object get(String interName) {
        return proxys.get(interName);
    }

    public void clear() {
        proxys.clear();
    }
}
