package com.hadluo.dubbo.invoker;

/***
 * 执行 方法信息 （参数，返回值,方法名）
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年4月27日 新建
 */
public interface Invocation {
    // 获取执行的方法名称
    String getMethodName();
    // 获取方法 参数 类型
    Class<?>[] getParameterTypes();
    //获取方法 参数
    Object[] getArguments();
}
