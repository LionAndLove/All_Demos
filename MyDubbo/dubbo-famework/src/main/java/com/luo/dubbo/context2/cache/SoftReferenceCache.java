package com.luo.dubbo.context2.cache;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

public class SoftReferenceCache<K, V> implements Cache<K, V> {

    private Hashtable<K, SoftReference<V>> hashtable = null;

    /***
     * 这个存 的是 ： 被标记回收的 SoftReference 对象。
     */
    private ReferenceQueue<V> refQue = null;

    public SoftReferenceCache() {
        this.hashtable = new Hashtable<K, SoftReference<V>>();
        this.refQue = new ReferenceQueue<V>();
    }

    public synchronized void put(K k, V v) {
        SoftReference<V> ref = new SoftReference<V>(v, this.refQue);
        hashtable.put(k, ref);
    }

    public synchronized V get(K key) {
        return hashtable.get(key).get();
    }
}
