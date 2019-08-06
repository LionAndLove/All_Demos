package com.hadluo.dubbo.extension;

import java.lang.reflect.Proxy;

public class ExtensionProxyFactory {
    @SuppressWarnings("unchecked")
    public static <T> T newFixProxy(Class<T> interClass, Object impl, ClassLoader classloader) {
        return (T) Proxy.newProxyInstance(classloader, new Class<?>[] { interClass }, new FixExtensionProxy(
                impl));
    }

    @SuppressWarnings("unchecked")
    public static <T> T newDynamicProxy(Class<T> interClass, ClassLoader classloader) {
        return (T) Proxy.newProxyInstance(classloader, new Class<?>[] { interClass },
                new DynamicExtensionProxy(interClass));
    }
}
