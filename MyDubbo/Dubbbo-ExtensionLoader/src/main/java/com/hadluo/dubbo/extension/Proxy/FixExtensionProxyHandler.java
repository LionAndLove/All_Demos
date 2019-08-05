package com.hadluo.dubbo.extension.Proxy;

import java.lang.reflect.Method;

/**
 * 代理真实对象
 */
public class FixExtensionProxyHandler implements ExtensionProxy {

    private Object target;

    public FixExtensionProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object obj = method.invoke(target, args);
        System.out.println("after");
        return obj;
    }

}
