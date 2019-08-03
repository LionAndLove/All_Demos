package com.luo.dubbo.rpc.resultset;

/***
 * 对 僵尸结果集(很久没有被用的 ) 的移除策略
 * 
 * @author HadLuo
 * @param <K>
 * @since JDK1.7
 * @history 2017年11月23日 新建
 */
public abstract class ChangeStatusStrategy<K> {

    public abstract <T> void change(Entry<T> entry) ;

    public <T> void setZombie(Entry<T> entry) {
        if (entry != null) {
            entry.setStatus(Entry.Zombie_Status);
        }
    }
}
