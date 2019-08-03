package com.luo.dubbo.rpc.frame;

import java.io.Serializable;
import java.lang.reflect.Method;

public class RpcClass implements Serializable {

    /** xx */
    private static final long serialVersionUID = 1L;

    private String className;

    private RpcMethod targetMethod;

    public static RpcClass toClass(Class<?> clazz, Method method,Object[] parameters) {
        RpcClass rpcClass = new RpcClass();
        rpcClass.setClassName(clazz.getName());
        rpcClass.setTargetMethod(RpcMethod.toMethod(method, parameters));
        return rpcClass ;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public RpcMethod getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(RpcMethod targetMethod) {
        this.targetMethod = targetMethod;
    }
}
