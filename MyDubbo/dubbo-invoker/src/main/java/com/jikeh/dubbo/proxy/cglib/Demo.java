package com.jikeh.dubbo.proxy.cglib;

import com.jikeh.dubbo.proxy.User;
import com.jikeh.dubbo.proxy.UserServiceImpl;
import org.springframework.cglib.proxy.Enhancer;

public class Demo {

    public static void main(String[] args) {
        User user = new User();
        user.setName("韩文腾");
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(new UserServiceInterceptor());
        UserServiceImpl userService = (UserServiceImpl) enhancer.create();
        userService.addUser(user);
    }

}
