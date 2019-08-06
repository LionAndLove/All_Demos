package com.hadluo.dubbo.extension;

import com.hadluo.dubbo.extension.proxy.DynamicExtensionProxyHandler;
import com.hadluo.dubbo.extension.proxy.FixExtensionProxyHandler;

import java.lang.reflect.Proxy;

/**
 * 获取动态代理对象的工厂方法
 */
public class ExtensionProxyFactory {

    /**
     * 1. 固定 动态代理类
     * @param interClass
     * @param impl
     * @param classloader
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newFixProxy(Class<T> interClass, Object impl, ClassLoader classloader) {
        return (T) Proxy.newProxyInstance(classloader, new Class<?>[] { interClass }, new FixExtensionProxyHandler(
                impl));
    }

    /**
     * 2. 动态 动态代理类
     * @param interClass
     * @param classloader
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T newDynamicProxy(Class<T> interClass, ClassLoader classloader) {
        return (T) Proxy.newProxyInstance(classloader, new Class<?>[] { interClass },
                new DynamicExtensionProxyHandler(interClass));
    }
}
