package com.luo.dubbo.rpc.resultset;

public class DefaultStrategy<K> extends ChangeStatusStrategy<K> {
    private static final long TIME_OUT = 1 * 60 * 60 * 1000;

    public <T> void change(Entry<T> entry) {
        // 比较时间
        if (System.currentTimeMillis() - entry.getBirthTime() > TIME_OUT) {
            setZombie(entry);
        }
    }
}