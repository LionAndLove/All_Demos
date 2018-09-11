package com.jikeh.controller;

import com.jikeh.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	@Autowired
	private UserServiceImpl userService;

	@RequestMapping("/getUserName")
	@ResponseBody
	public String getUserName(Long userId) {
//		return userService.getUserName(userId);//缓存失效
		return userService.getUserNameByCache(userId);//缓存有效
	}
	
}
