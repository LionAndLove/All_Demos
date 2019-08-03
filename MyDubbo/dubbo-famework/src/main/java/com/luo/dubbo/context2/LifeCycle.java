package com.luo.dubbo.context2;

public interface LifeCycle {

    /****
     * 组件被初始化调用
     * 
     * @author HadLuo 2017年12月15日 新建
     */
    public void onCreate();

    /***
     * 组件被销毁时调用
     * 
     * @author HadLuo 2017年12月15日 新建
     */
    public void onDestory();
}
