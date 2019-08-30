package com.jikeh.dubbo.proxy;

public class UserServiceImpl implements UserService {

    @Override
    public void addUser(User user) {
        System.out.println("user生成成功，user = " + user.toString());
    }

}
