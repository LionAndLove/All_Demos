package com.jikeh;

import com.jikeh.spout.MessageSpout;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.bolt.KafkaBolt;
import org.apache.storm.kafka.bolt.mapper.FieldNameBasedTupleToKafkaMapper;
import org.apache.storm.kafka.bolt.selector.DefaultTopicSelector;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.Properties;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
public class KafkaProducerTopology {

    private static final String topicName = "jikeh_producer";

    public static void main(String[] args) {

        TopologyBuilder builder = new TopologyBuilder();

        //1、spout源数据
        MessageSpout messageSpout = new MessageSpout(new Fields("key", "message"));

        builder.setSpout("spout", messageSpout);

        //2、写入kafka
        //set producer properties.
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.199.147:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        KafkaBolt bolt = new KafkaBolt()
                .withProducerProperties(props)
                .withTopicSelector(new DefaultTopicSelector(topicName))
//                .withTupleToKafkaMapper(new FieldNameBasedTupleToKafkaMapper("key", "value"))
                ;
        builder.setBolt("bolt", bolt).shuffleGrouping("spout");

        //3、本地模式运行我们的storm作业
        Config config = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("KafkaConsumerTopology", config, builder.createTopology());

    }
}
