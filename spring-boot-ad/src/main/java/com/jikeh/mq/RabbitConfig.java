package com.jikeh.mq;

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

    /**
     * 正常队列：
     */
    public final static String queueName = "ad_queue";

    /**
     * 过期队列：
     */
    public final static String queueTtlName = "ad_ttl_queue";

    //过期队列TTL：
    public final static int QUEUE_TTL = 4000;

    /**
     * 死信队列：
     */
    public final static String deadQueueName = "ad_send_dead_queue";
    public final static String deadRoutingKey = "ad_send_dead_routing_key";
    public final static String deadExchangeName = "ad_send_dead_exchange";

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

    //建立过期队列与死信队列的关系：当过期队列过期了之后，自动进入死信队列
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

    /**
     * 死信队列：存储发送异常的信息
     */
    @Bean
    public Queue deadQueue() {
        Queue queue = new Queue(deadQueueName, true);
        return queue;
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(deadExchangeName);
    }

    @Bean
    public Binding bindingDeadExchange(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(deadRoutingKey);
    }

}
