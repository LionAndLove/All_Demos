package com.jikeh;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 更多资料分享下载，请关注：
 * 极客慧：http://www.jikeh.cn
 *  QQ群：375412858
 */
public class RabbitConsumer {

    private static final String QUUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 5672;//默认端口号为5672

    private static final String USER_NAME = "guest";
    private static final String USER_PWD = "guest";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //初始化TCP连接参数
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(USER_NAME);
        factory.setPassword(USER_PWD);

        Address[] addresses = new Address[]{
            new Address(IP_ADDRESS, PORT)
        };

        //创建连接
        Connection connection = factory.newConnection(addresses);

        //创建信道
        final Channel channel = connection.createChannel();

        //设置客户端最多接收未被ack的消息的个数
        channel.basicQos(64);

        //消费消息
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.printf("receive message：" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /**
                 * true to acknowledge all messages up to and including the supplied delivery tag; false to acknowledge just the supplied delivery tag.
                 * true：确认包括传输标记之前的所有信息；
                 * false：仅确认该传输标记的消息
                 */
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(QUUE_NAME, consumer);

        //rabbitmq从队列中删除相应已经被确认的消息

        //等待回掉函数执行完毕之后，关闭资源
        //持续等待接收消息5s
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();

    }

}
