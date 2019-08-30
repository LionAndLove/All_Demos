package com.jikeh.dubbo.invoker.invoker;

public class RpcResult implements Result {
    private Object object;

    public RpcResult(Object object) {
        this.object = object;
    }
    @Override
    public Object getResult() {
        return object;
    }
}
