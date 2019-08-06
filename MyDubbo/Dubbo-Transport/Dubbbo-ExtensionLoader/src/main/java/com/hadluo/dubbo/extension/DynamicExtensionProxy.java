package com.hadluo.dubbo.extension;

import java.lang.reflect.Method;
import java.util.HashMap;

public class DynamicExtensionProxy implements ExtensionProxy {
    /***
     * key: 方法名称 value: Extension key
     */
    private HashMap<String, String> methodConfiguration = new HashMap<String, String>();

    private Class<?> extensionInterfaceClass;

    public DynamicExtensionProxy(Class<?> extensionInterfaceClass) {
        this.extensionInterfaceClass = extensionInterfaceClass;
        parserAdaptive(extensionInterfaceClass);
    }

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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String extensionKey = methodConfiguration.get(method.getName());
        if (extensionKey == null) {
            throw new UnsupportedOperationException(extensionInterfaceClass.getName() + "接口的 "
                    + method.getName() + "方法 必须配置adaptive注解");
        }
        Object object = ExtensionLoader.getExtensionLoader(extensionInterfaceClass).getAdaptiveExtension(
                extensionKey);
        if (object == null) {
            throw new RuntimeException("找不到  extension key :" +extensionKey);
        }
        return method.invoke(object, args);
    }

}
