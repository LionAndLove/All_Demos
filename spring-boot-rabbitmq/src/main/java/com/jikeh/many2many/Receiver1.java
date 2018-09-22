package com.jikeh.many2many;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "many")
public class Receiver1 {

    @RabbitHandler
    public void process(String many) {
        System.out.println("Receiver 1: " + many);
    }

}
