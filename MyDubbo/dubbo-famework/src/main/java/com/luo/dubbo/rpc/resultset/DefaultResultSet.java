package com.luo.dubbo.rpc.resultset;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class DefaultResultSet<K, V> implements ResultSet<K, V> {

    private static final int timer_execute_time = 1 * 60 * 1000;

    private ChangeStatusStrategy<K> defaultStrategy;

    private Timer timer;
    private HashMap<K, Entry<V>> map, temporaryMap;
    private Set<K> temporarySet;

    private final Object lock = new Object();

    public DefaultResultSet() {
        allocate();
        initialization();
    }

    private void allocate() {
        map = new HashMap<K, Entry<V>>();
        temporaryMap = new HashMap<K, Entry<V>>();
        temporarySet = new HashSet<K>();
    }

    public void setDefaultStrategy(ChangeStatusStrategy<K> defaultStrategy) {
        this.defaultStrategy = defaultStrategy;
    }

    private void initialization() {
        setDefaultStrategy(new DefaultStrategy<K>());
        // TODO Auto-generated method stub
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    cleanup();
                } catch (Exception e) {
                }
            }
        }, 0, timer_execute_time);
    }

    /***
     * 清扫 不可用对象（僵尸对象，大对象）
     * 
     * @author HadLuo 2017年11月23日 新建
     */
    private void cleanup() {
        Map<K, Entry<V>> map = deepCopy();
        temporarySet.clear();
        Entry<V> entry;
        for (K k : map.keySet()) {
            entry = map.get(k);
            defaultStrategy.change(entry);
            if (entry.getStatus() == Entry.Zombie_Status) {
                temporarySet.add(k);
            }
        }
        for (K k : temporarySet) {
            remove(k);
        }
    }

    public V fromResultSet(K k) {
        synchronized (lock) {
            V v = map.get(k).getT();
            if (v != null) {
                map.remove(k);
            }
            return v;
        }
    }

    public void putResultSet(K k, V v) {
        synchronized (lock) {
            if (v == null) {
                return;
            }
            map.put(k, new Entry<V>(v));
        }
    }

    private void remove(K k) {
        synchronized (lock) {
            map.remove(k);
        }
    }

    private Map<K, Entry<V>> deepCopy() {
        temporaryMap.clear();
        synchronized (lock) {
            temporaryMap.putAll(map);
            return temporaryMap;
        }
    }
}
