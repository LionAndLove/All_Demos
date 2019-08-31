package com.jikeh.dubbo.InvokerChain.filter;

import com.jikeh.dubbo.InvokerChain.Invoker.Invoker;

public interface Filter {

    String doInvoke(Invoker<?> invoker, String invocation);

}