package com.hadluo.dubbo.extension;

import java.lang.reflect.Method;

public class FixExtensionProxy implements ExtensionProxy {

    private Object target;

    public FixExtensionProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target, args);
    }

}
