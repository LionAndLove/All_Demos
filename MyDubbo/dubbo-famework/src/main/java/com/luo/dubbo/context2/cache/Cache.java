package com.luo.dubbo.context2.cache;

/***
 * 缓存
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月17日 新建
 */
public interface Cache<K, V> {

    public V get(K key);

    public void put(K k, V v);

}
