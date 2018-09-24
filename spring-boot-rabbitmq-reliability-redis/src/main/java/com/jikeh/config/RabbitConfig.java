package com.jikeh.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public final static String queueName = "ad_queue";

    /**
     * 过期队列：
     */
    public final static String queueTtlName = "ad_receive_ttl_queue";

    //过期队列TTL：
    public final static int QUEUE_TTL = 5000;

    /**
     * 死信队列：
     */
    public final static String deadRoutingKey = "ad_queue";
    public final static String deadExchangeName = "ad_receive_dead_exchange";

    /**
     * 死信队列 交换机标识符
     */
    public static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

    /**
     * 死信队列交换机绑定键标识符
     */
    public static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 队列的TTL标志符：
     */
    public static final String MESSAGE_TTL = "x-message-ttl";

    /**
     * 正常消费队列：必须显性声明
     */
    @Bean
    public Queue adQueue() {
        Queue queue = new Queue(queueName, true);
        return queue;
    }

    @Bean
    public Queue ttlQueue() {
        //将普通队列绑定到私信交换机上
        Map<String, Object> args = new HashMap<>(2);
        args.put(DEAD_LETTER_QUEUE_KEY, deadExchangeName);
        args.put(DEAD_LETTER_ROUTING_KEY, deadRoutingKey);
        args.put(MESSAGE_TTL, QUEUE_TTL);
        Queue queue = new Queue(queueTtlName, true, false, false, args);
        return queue;
    }

    //声明死信交换机：
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(deadExchangeName);
    }

    //建立死信交换机与正常消费队列的关系：
    @Bean
    public Binding bindingDeadExchange(Queue adQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(adQueue).to(deadExchange).with(deadRoutingKey);
    }

}
