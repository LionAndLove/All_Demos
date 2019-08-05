package com.hadluo.dubbo.extension;

public class Holder<T> {

    private T value;

    public Holder(T value) {
        this.value = value;
    }

    public Holder() {
        // TODO Auto-generated constructor stub
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
