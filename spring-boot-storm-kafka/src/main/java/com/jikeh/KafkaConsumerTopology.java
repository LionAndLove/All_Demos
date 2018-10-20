package com.jikeh;

import com.jikeh.bolt.KafkaConsumerBolt;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.UUID;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
public class KafkaConsumerTopology {

    public static void main(String[] args) {

        //1、Kafka Spout

        //这个地方其实就是kafka配置文件里边的zookeeper.connect这个参数，可以去那里拿过来。
        String brokerZkStr = "localhost:2181";
        ZkHosts zkHosts = new ZkHosts(brokerZkStr);

        String topic = "jikeh";

        //汇报offset信息的root路径
        String offsetZkRoot = "/" + topic;

        //存储该spout id的消费offset信息,譬如以topoName来命名
        String offsetZkId = UUID.randomUUID().toString();

        SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, topic, offsetZkRoot, offsetZkId);
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout spout = new KafkaSpout(kafkaConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", spout);
        builder.setBolt("bolt", new KafkaConsumerBolt()).shuffleGrouping("spout");

        Config config = new Config();

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("KafkaConsumerTopology", config, builder.createTopology());

    }
}

