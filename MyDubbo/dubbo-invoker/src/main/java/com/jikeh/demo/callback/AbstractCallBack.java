package com.jikeh.demo.callback;

/***
 * 支持回调的抽象类
 * 
 * @author hanwenteng
 */
public abstract class AbstractCallBack {

    /**
     * 回调方法：
     *
     * @return
     */
    public abstract void doInvoke();

    public void invoker() {
        //前置操作：
        doInvoke();
        //后置操作：
    }

}
