package com.jikeh.controller;

import com.jikeh.producer.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendKafkaController {

	@Autowired
	Sender sender;

	/**
	 * 消息发送测试：http://localhost:8088/send
	 * @return
	 * @throws InterruptedException
	 */
	@RequestMapping("/send")
	@ResponseBody
	public String send() throws InterruptedException {
		for (int i = 0; i < 50; i++){
			sender.sendMessage();
			Thread.sleep(500);
		}
		return "success";
	}
	
}
