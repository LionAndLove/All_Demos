package com.luo.provider.serviceimpl;

import org.springframework.stereotype.Service;

import com.luo.provider.bean.User;
import com.luo.provider.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {
    public String login(User user) {
        System.out.println("用户登录: " + user);

        return "ok";
    }

    public void register() {
        // TODO Auto-generated method stub
        System.out.println("用户注册");
    }
}
