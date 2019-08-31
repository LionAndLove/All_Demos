package com.jikeh.dubbo.InvokerChain.Invoker;

public class UserInvoker implements Invoker{

    @Override
    public String invoke(String invocation) {
        return "手写Invoker"+invocation;
    }

    public static class SubUserInvoker {
        private String name;
    }

}
