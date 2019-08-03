package com.luo.dubbo.rpc.resultset;

/***
 * 结执行果集
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月22日 新建
 */
public interface ResultSet<K, V> {

    /***
     * 从结果集中取出数据
     * 
     * @param k
     * @return
     * @author HadLuo 2017年11月22日 新建
     */
    public V fromResultSet(K k);
    
    /***
     * 放入结果集数据
     * @param k
     * @param v
     * @author HadLuo  2017年11月22日 新建
     */
    public void putResultSet(K k , V v);
}
