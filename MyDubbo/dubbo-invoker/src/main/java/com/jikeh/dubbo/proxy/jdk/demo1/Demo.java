package com.jikeh.dubbo.proxy.jdk.demo1;

import com.jikeh.dubbo.proxy.User;
import com.jikeh.dubbo.proxy.UserService;
import com.jikeh.dubbo.proxy.UserServiceImpl;

import java.lang.reflect.Proxy;

public class Demo {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserService userServiceProxy = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(), userService.getClass().getInterfaces(), new UserServiceInterceptor(userService));

        User user = new User();
        user.setName("韩文腾");
        userServiceProxy.addUser(user);
    }

}
