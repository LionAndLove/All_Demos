package com.jikeh.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
			/**
			 * 我们这里仅通过打印日志、发送邮件来预警，并没有实现自动重试机制：
			 * 1、将发送失败重新发送到一个队列中：fail-queue，然后可以定时对这些消息进行重发
			 * 2、在本地定义一个缓存map对象，定时进行重发
			 * 3、为了更安全，可以将所有发送的消息保存到db中，并设置一个状态(是否发送成功)，定时扫描检查是否存在未成功发送的信息
			 * 这块知识，我们后期讲"分布式事务"的时候，在深入讲解这块内容
			 */
			emailLogger.error("send ack fail, cause = {}, correlationData = {}", cause, correlationData.getId());

			//将异常信息发送到一个过期队列：
			send(RabbitConfig.queueTtlName, correlationData.getId());
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
		String str = new String(message.getBody());
		send(RabbitConfig.queueTtlName, str);
	}

}