package com.jikeh.dubbo.InvokerChain.filter;

import com.jikeh.dubbo.InvokerChain.Invoker.Invoker;

public class EchoFilter2 implements Filter {
    @Override
    public String doInvoke(Invoker<?> invoker, String invocation) {
        System.out.println("执行过滤链2");
        return invoker.invoke(invocation);
    }
}
