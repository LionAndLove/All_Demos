package com.hadluo.dubbo.schema;

import org.springframework.context.ApplicationContext;

public class Holder<T> {

    private String id;

    private T value;

    public Holder(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T get() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public void setValue(ApplicationContext context) {
        this.value = (T) context.getBean(id);
    }

}
