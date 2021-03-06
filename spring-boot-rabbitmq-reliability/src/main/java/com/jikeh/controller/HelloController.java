package com.jikeh.controller;

import com.jikeh.base.HelloSender;
import com.jikeh.config.RabbitConfig;
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
		helloSender.send(null, RabbitConfig.queueName);
		return "success";
	}

	/**
	 * 下面模拟生产者发送状态的集中场景：
	 */

	//1、exchange, queue 都正确, confirm被回调, ack=true
	@RequestMapping("/send1")
	@ResponseBody
	public String send1() {
		helloSender.send(null, RabbitConfig.queueName);
		return "success";
	}

	//2、exchange 错误, queue 正确, confirm被回调, ack=false
	@RequestMapping("/send2")
	@ResponseBody
	public String send2() {
		helloSender.send("fail-exchange", RabbitConfig.queueName);
		return "success";
	}

	//3、exchange 正确, queue 错误, confirm被回调, ack=true; return被回调 replyText:NO_ROUTE
	@RequestMapping("/send3")
	@ResponseBody
	public String send3() {
		helloSender.send(null, "fail-queue");
		return "success";
	}

	//4、exchange 错误, queue 错误, confirm被回调, ack=false
	@RequestMapping("/send4")
	@ResponseBody
	public String send4() {
		helloSender.send("fail-exchange", "fail-queue");
		return "success";
	}

}
