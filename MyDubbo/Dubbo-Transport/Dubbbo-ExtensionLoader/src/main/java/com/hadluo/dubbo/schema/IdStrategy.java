package com.hadluo.dubbo.schema;

public interface IdStrategy {
    public String id(Class<?> beanClass);
}
