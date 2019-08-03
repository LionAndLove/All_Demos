package com.luo.dubbo.context2;

public abstract class Handler implements Watcher{
    private DubboContext context;

    public abstract void handle();

    public Handler(DubboContext context) {
        this.context = context;
    }

    DubboContext context() {
        return context;
    }
}
