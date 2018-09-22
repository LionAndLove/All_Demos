package com.jikeh.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AdvanceRabbitConfig {

    public final static String queueName = "advance_queue";

    public final static String routingKey = "advance_routing_key";

    public final static String exchangeName = "advance_exchange";

    @Bean
    public Queue advanceQueue() {
        return new Queue(queueName);
    }

    @Bean
    DirectExchange advanceExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    Binding bindingExchangeMessage(Queue advanceQueue, DirectExchange advanceExchange) {
        return BindingBuilder.bind(advanceQueue).to(advanceExchange).with(routingKey);
    }
}
