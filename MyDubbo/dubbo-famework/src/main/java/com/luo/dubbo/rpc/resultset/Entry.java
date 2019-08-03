package com.luo.dubbo.rpc.resultset;

public class Entry<T> {
    /***
     * 僵尸状态
     */
    public static final byte Zombie_Status = 2;

    /***
     * 出生时间
     */
    private long birthTime;

    /***
     * 真实对象
     */
    private T t;

    /***
     * 状态
     */
    private byte status;

    public Entry(T t) {
        this.t = t;
        this.birthTime = System.currentTimeMillis();
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getBirthTime() {
        return birthTime;
    }
}
