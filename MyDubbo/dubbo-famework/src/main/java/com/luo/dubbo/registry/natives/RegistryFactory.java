package com.luo.dubbo.registry.natives;

import com.luo.dubbo.exception.NotInstanceRegistryException;

public class RegistryFactory {

    public static Registry createRegistry(Class<?> implClass) throws InstantiationException, IllegalAccessException {
        return (Registry) implClass.newInstance();
    }

    public static Registry createRegistry(String implClassName) {
        try {
            return createRegistry(Class.forName(implClassName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotInstanceRegistryException("不能实例化注册中心  class:" + implClassName);
        }
    }
}
