package com.jikeh.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback{

	private final Logger emailLogger = LoggerFactory.getLogger("emailLogger");

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(String routingKey, String content) {
		this.rabbitTemplate.setMandatory(true);
		this.rabbitTemplate.setConfirmCallback(this);
		this.rabbitTemplate.setReturnCallback(this);
		this.rabbitTemplate.setRoutingKey(routingKey);

		//这样我们就能知道，发送失败的是哪条消息了
		this.rabbitTemplate.correlationConvertAndSend(content, new CorrelationData(content));
//		this.rabbitTemplate.convertAndSend(routingKey, content);
	}

	/**
	 * 确认后回调:
	 * @param correlationData
	 * @param ack
	 * @param cause
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		if (!ack) {
			emailLogger.error("send ack fail, cause = {}, correlationData = {}", cause, correlationData.getId());
		} else {
			System.out.println("send ack success");
		}
	}

	/**
	 * 失败后return回调：
	 *
	 * @param message
	 * @param replyCode
	 * @param replyText
	 * @param exchange
	 * @param routingKey
	 */
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		emailLogger.error("send fail return-message = " + new String(message.getBody()) + ", replyCode: " + replyCode + ", replyText: " + replyText + ", exchange: " + exchange + ", routingKey: " + routingKey);
	}

}