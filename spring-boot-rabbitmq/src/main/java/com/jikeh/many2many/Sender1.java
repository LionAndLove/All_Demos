package com.jikeh.many2many;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender1 {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(String str) {
		String context = "spirng boot many queue"+" ****** " + str;
		System.out.println("Sender1 : " + context);
		this.rabbitTemplate.convertAndSend("many", context);
	}

}