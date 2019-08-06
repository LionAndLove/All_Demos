package com.hadluo.dubbo.invoker;

/***
 * 本地 普通调用 的 方法信息
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年4月27日 新建
 */
public class NativeInvocation implements Invocation {

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] arguments;

    public NativeInvocation(String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        super();
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.arguments = arguments;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }
    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }
}
