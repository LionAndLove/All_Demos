package com.jikeh.producer;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.domain.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class Sender {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    private Long id = 0L;

    public void sendMessage(){
        Message m = new Message();
        if(id % 3 == 0){
            id = 1L;
        } else if(id % 3 == 1) {
            id = 2L;
        } else {
            id = 3L;
        }
        m.setId(id++);
        m.setMsg(UUID.randomUUID().toString());
        m.setSendTime(new Date());
        kafkaTemplate.send("jikeh", JSONObject.toJSONString(m));
    }
}
