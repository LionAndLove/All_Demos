package com.hadluo.dubbo.extension.proxy;

import com.hadluo.dubbo.extension.spi.Adaptive;
import com.hadluo.dubbo.extension.ExtensionLoader;
import com.hadluo.dubbo.extension.spi.SPI;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 动态代理 代理对象：即 嵌套代理
 */
public class DynamicExtensionProxyHandler implements ExtensionProxy {
    /**
     * 存储所有@Adaptive修饰的方法
     * key: 方法名称 value: Extension key
     */
    private HashMap<String, String> methodConfiguration = new HashMap<String, String>();

    private Class<?> extensionInterfaceClass;

    public DynamicExtensionProxyHandler(Class<?> extensionInterfaceClass) {
        this.extensionInterfaceClass = extensionInterfaceClass;
        parserAdaptive(extensionInterfaceClass);
    }

    /**
     * 获取默认key
     * @param srcClass
     */
    private void parserAdaptive(Class<?> srcClass) {
        for (Method m : extensionInterfaceClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(Adaptive.class)) {
                String value = m.getAnnotation(Adaptive.class).value();
                if (value == null || value.isEmpty()) {
                    // 取 spi 的值
                    value = extensionInterfaceClass.getAnnotation(SPI.class).value();
                    if (value == null || value.isEmpty()) {
                        throw new IllegalArgumentException("必须配置 Extension 的key");
                    }
                }
                methodConfiguration.put(m.getName(), value);
            }
        }
    }

    /**
     * 双层代理：
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取默认key
        String extensionKey = methodConfiguration.get(method.getName());
        if (extensionKey == null) {
            throw new UnsupportedOperationException(extensionInterfaceClass.getName() + "接口的 "
                    + method.getName() + "方法 必须配置adaptive注解");
        }
        //调用单层代理
        Object object = ExtensionLoader.getExtensionLoader(extensionInterfaceClass).getAdaptiveExtension(extensionKey);
        if (object == null) {
            throw new RuntimeException("找不到  extension key :" +extensionKey);
        }
        System.out.println("dynamic-before");
        Object obj = method.invoke(object, args);
        System.out.println("dynamic-after");
        return obj;
    }

}
