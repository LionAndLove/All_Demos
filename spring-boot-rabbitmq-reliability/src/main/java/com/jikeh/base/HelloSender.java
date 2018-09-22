package com.jikeh.base;

import com.jikeh.config.RabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send() {
		String context = "你好现在是 " + new Date();
		System.out.println("send content = " + context);
		this.rabbitTemplate.setMandatory(true);
		this.rabbitTemplate.setConfirmCallback(this);
		this.rabbitTemplate.setReturnCallback(this);
		this.rabbitTemplate.convertAndSend(RabbitConfig.queueName, context);
	}

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		if (!ack) {
			System.out.println("send fail" + cause + correlationData.toString());
		} else {
			System.out.println("send success");
		}
	}

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		System.out.println("sender return success" + message.toString()+"==="+replyCode+"==="+replyText+"==="+exchange+"==="+routingKey);
	}
}