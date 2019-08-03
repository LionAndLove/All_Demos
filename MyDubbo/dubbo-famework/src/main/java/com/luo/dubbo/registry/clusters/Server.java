package com.luo.dubbo.registry.clusters;

import com.luo.dubbo.registry.validator;

/***
 * 提供者机器硬件信息
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月8日 新建
 */
public final class Server implements validator {
    /** xx */
    private static final long serialVersionUID = -7462020383000704506L;

    /***
     * 剩余内存
     */
    private float cpu;

    /***
     * 剩余 cpu 使用量
     */
    private float memory;

    public Server() {
        // 假设 
        setCpu(100);
        setMemory(100);
    }

    public float getCpu() {
        return cpu;
    }

    public void setCpu(float cpu) {
        this.cpu = cpu;
    }

    public float getMemory() {
        return memory;
    }

    public void setMemory(float memory) {
        this.memory = memory;
    }

    public boolean validate() {
        // TODO 这里 可以判断 cpu 和内存 是否 过载，过载则不让通过

        return true;
    }

    @Override
    public String toString() {
        return "Server [cpu=" + cpu + ", memory=" + memory + "]";
    }
}
