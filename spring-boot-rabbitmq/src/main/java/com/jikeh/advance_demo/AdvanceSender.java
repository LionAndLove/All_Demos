package com.jikeh.advance_demo;

import com.jikeh.config.AdvanceRabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AdvanceSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {
		String context = "hello " + new Date();
		System.out.println("Sender : " + context);
		this.rabbitTemplate.convertAndSend(AdvanceRabbitConfig.exchangeName, AdvanceRabbitConfig.routingKey, context);
	}

}