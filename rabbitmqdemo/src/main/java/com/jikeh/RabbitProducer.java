package com.jikeh;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 更多资料分享下载，请关注：
 * 极客慧：http://www.jikeh.cn
 *  QQ群：375412858
 */
public class RabbitProducer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String EXCHANGE_TYPE = "direct";
    private static final String ROUTING_KEY = "routingkey_demo";
    private static final String QUUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 5672;//默认端口号为5672

    private static final String USER_NAME = "guest";
    private static final String USER_PWD = "guest";

    public static void main(String[] args) throws IOException, TimeoutException {

        //初始化TCP连接参数
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername(USER_NAME);
        factory.setPassword(USER_PWD);

        //创建连接
        Connection connection = factory.newConnection();

        //创建信道
        Channel channel = connection.createChannel();

        /**
         * 语法格式：exchangeDeclare(String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments)
         * 创建一个type="direct"、持久化的、非自动删除的交换器
         *
         */
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, null);

        /**
         * 语法格式：queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * 创建一个持久化、非排他的、非自动删除的队列
         * exclusive：排他的 由该客户端独占，一般设为false
         * autoDelete：消息消费完，该队列就会被删除，一般为false
         */
        channel.queueDeclare(QUUE_NAME, true, false, false, null);

        //将交换器与队列通过路由键绑定
        channel.queueBind(QUUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        //发送一条持久化的消息
        //MessageProperties.PERSISTENT_TEXT_PLAIN：是以纯文本格式发送
        String message = "Hello World!";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        //关闭信道
        channel.close();

        //关闭连接
        connection.close();

    }


}
