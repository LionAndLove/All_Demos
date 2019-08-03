package com.luo.dubbo.rpc.frame;

import java.io.Serializable;
import java.lang.reflect.Method;

public class RpcMethod implements Serializable {

    /** xx */
    private static final long serialVersionUID = -8635790262564891550L;

    /***
     * 参数
     */
    private Object[] parameters;

    /***
     * 返回值
     */
    private Object returnValue;

    /***
     * 方法名
     */
    private String name;

    public static RpcMethod toMethod(Method method, Object[] parameters) {
        RpcMethod rpcMethod = new RpcMethod();
        rpcMethod.setName(method.getName());
        rpcMethod.setParameters(parameters);
        return rpcMethod;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
