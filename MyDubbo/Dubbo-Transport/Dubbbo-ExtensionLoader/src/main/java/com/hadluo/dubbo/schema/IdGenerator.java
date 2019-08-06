package com.hadluo.dubbo.schema;

public class IdGenerator {
    public static String generateId(IdStrategy strategy, Class<?> beanClass) {
        return strategy.id(beanClass);
    }
}
