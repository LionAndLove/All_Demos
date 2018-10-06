package com.jikeh.consumer;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.domain.Message;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

    @KafkaListener(topics = "jikeh")
    public void processMessage(String content) {
        Message m = JSONObject.parseObject(content, Message.class);
        System.out.println("接受信息："+m.getMsg());
    }
}
