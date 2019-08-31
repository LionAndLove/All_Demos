package com.jikeh.dubbo.InvokerChain.Invoker;

public interface Invoker<T> {

    String invoke(String invocation);

}
