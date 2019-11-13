package com.jikeh.controller;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	private static final Logger logger = LogManager.getLogger(HelloController.class);

	@RequestMapping("/hello")
	@ResponseBody
	public String hello(String name) {
		if (logger.isDebugEnabled()) {

			logger.debug("12345");
			logger.debug("xxxxx {}", "httlo");

		}
		logger.info("xxxxx {}", "httl info");
		return "hello, " + name;
	}
	
}
