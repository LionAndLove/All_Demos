package com.jikeh.controller;

import com.jikeh.base.HelloSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@Autowired
	private HelloSender helloSender;

	@RequestMapping("/base")
	@ResponseBody
	public String hello() {
		helloSender.send();
		return "success";
	}
	
}
