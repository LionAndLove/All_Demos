package com.jikeh.dubbo.proxy.jdk.demo1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserServiceInterceptor implements InvocationHandler {

    private Object target;

    public UserServiceInterceptor() {
    }

    public UserServiceInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object ret = method.invoke(target, args);
        System.out.println("after");
        return ret;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
