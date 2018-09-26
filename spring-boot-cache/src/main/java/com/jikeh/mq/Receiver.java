package com.jikeh.mq;

import com.alibaba.fastjson.JSON;
import com.jikeh.config.RabbitConfig;
import com.jikeh.model.AdLock;
import com.jikeh.model.AdMessage;
import com.jikeh.service.UpdateRedisServiceImpl;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

@Component
@RabbitListener(queues = RabbitConfig.queueName)
public class Receiver {

    private Logger logger = LoggerFactory.getLogger(Receiver.class);

    private final Logger emailLogger = LoggerFactory.getLogger("emailLogger");

    @Resource
    UpdateRedisServiceImpl updateRedisService;

    @Autowired
    private Sender sender;

    @RabbitHandler
    public void process(String content, Channel channel, Message message) {

        logger.info("handle msg begin = {}", content);

        AdMessage adMessage = JSON.parseObject(content, AdMessage.class);
        Long id = adMessage.getId();
        String uuid = adMessage.getUuidKey();
        String adStr = adMessage.getContent();
        if(updateRedisService.isExist(uuid)){
            //重复消息直接丢弃
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                logger.info("handle msg finished = {}", content);
                return;
            }
        }
        updateRedisService.setUuid(uuid);

        int retryTimes = 0;
        while (retryTimes < 5) {
            //消费者做幂等处理(当然这只是对单台机器而言没有问题，如果是分布式集群环境，这种是不行的，后续我们会继续优化这块)：防止相同类型的广告id更新问题
            synchronized (AdLock.cacheLock) {
                //更新redis数据：
                if(!updateRedisService.updateRedis(id, adStr)){
                    retryTimes++;
                }else {
                    break;
                }
            }
        }

        if (retryTimes >= 3) {
            //当有多次更新失败的时候，发送邮件通知：
            emailLogger.error("处理MQ[" + content + "]失败[" + retryTimes + "]次");
        }

        try {
            if (retryTimes >= 5) {
                //当有很多次更新失败的时候，将其放入缓存队列，等待延迟消费：
                sender.send(RabbitConfig.queueTtlName, content);

                //既然已经重新发送了，也算一种正常消费了
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }else {
                //告诉服务器收到这条消息 已经被我消费了 可以在队列删掉；否则消息服务器以为这条消息没处理掉 后续还会在发
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            }

        } catch (Exception e){
            logger.error("消息确认失败", e);
        }

        logger.info("handle msg finished = {}", content);

    }

}
