package com.jikeh.controller;

import com.jikeh.advance_demo.AdvanceSender;
import com.jikeh.base.HelloSender;
import com.jikeh.many2many.Sender1;
import com.jikeh.many2many.Sender2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

	@Autowired
	private HelloSender helloSender;

	@Autowired
	private Sender1 sender1;

	@Autowired
	private Sender2 sender2;

	@Autowired
	private AdvanceSender advanceSender;

	@RequestMapping("/base")
	@ResponseBody
	public String hello() {
		helloSender.send();
		return "success";
	}

	@RequestMapping("/one2many")
	@ResponseBody
	public String one2many() {
		for (int i = 0; i < 20; i++){
			sender1.send("sender1：" + i);
		}
		return "success";
	}

	@RequestMapping("/many2many")
	@ResponseBody
	public String many2many() {
		for (int i = 0; i < 20; i++){
			sender1.send("sender1：" + i);
			sender2.send("sender2：" + i);
		}
		return "success";
	}

	@RequestMapping("/advance")
	@ResponseBody
	public String advance() {
		advanceSender.send();
		return "success";
	}
	
}
