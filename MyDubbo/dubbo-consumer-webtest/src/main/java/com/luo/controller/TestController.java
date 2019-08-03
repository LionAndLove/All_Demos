package com.luo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.luo.provider.bean.User;
import com.luo.provider.service.UserService;

@Controller
public class TestController {
    @Autowired
    private UserService userService;
    
    public TestController() {
        System.out.println("TestController" + userService);
    }

    @RequestMapping("/test")
    @ResponseBody
    public String login() {
        User user = new User();
        user.setAge(1);
        user.setName("xxx");
        ;
        String ret = userService.login(user);

        System.out.println(ret);
        return ret;
    }

}
