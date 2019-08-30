package com.jikeh.dubbo.invoker.invoker;

/***
 * 执行器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年4月27日 新建
 */
public interface Invoker<T>{

    Result invoker(Invocation invocation);
    // 省略其它 dubbo中的方法
}
