package com.luo.dubbo.rpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyFactory {

    private final static ClassGenerator generator1 = ClassGenerator.newInstance();
    private final static ClassGenerator generator2 = ClassGenerator.newInstance();

    @SuppressWarnings("unchecked")
    public static <T> T newProxy(Class<T> inter, InvocationHandler invocationHandler) {
        try {
            /**
             * JDK动态代理
             */
            return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    buildImplClass(inter).getInterfaces(), invocationHandler);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("动态生成类异常， cause:" + e.getMessage());
        }
    }

    /***
     * 构建 接口实现类
     * 
     * @param inter
     * @return
     * @author HadLuo 2017年11月22日 新建
     */
    public static Class<?> buildImplClass(Class<?> inter) {
        generator2.addInterface(inter);
        generator2.setClassName("$proxy" + System.currentTimeMillis());
        Class<?> clazz = generator2.toClass();
        generator2.release();
        return clazz;
    }

    /***
     * 构建 接口实现类
     * 
     * @param inter
     * @return
     * @author HadLuo 2017年11月22日 新建
     */
    public static Class<?> buildImplClasses(Class<?>... inter) {
        for (Class<?> clazz : inter) {
            if (clazz.isInterface()) {
                generator1.addInterface(clazz);
            } else {
                generator1.setSuperClass(clazz);
            }
        }
        generator1.setClassName("$proxy" + System.currentTimeMillis());
        Class<?> clazz = generator1.toClass();
        generator1.release();
        return clazz;
    }
}
