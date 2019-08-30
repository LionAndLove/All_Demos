package com.jikeh.demo.callback;

/**
 * 回调函数测试类：
 */
public class demo {

    public static void main(String[] args) {
        AbstractCallBack abstractCallBack = demo.getInvoker();
        abstractCallBack.invoker();
    }

    private static <T> AbstractCallBack getInvoker() {
        return new AbstractCallBack() {
            UserServiceImpl userService = new UserServiceImpl();
            @Override
            public void doInvoke() {
                userService.login();
            }
        };
    }

}
