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
        //ZkStr 字符串格式是 ip:port（例如：localhost:2181）.brokerZkPath 是存储所有 topic 和 partition信息的zk 根路径.默认情况下，Kafka使用 /brokers路径.
        String brokerZkStr = "192.168.199.147:2181";
        ZkHosts zkHosts = new ZkHosts(brokerZkStr);

        String topic = "jikeh";

        //汇报offset信息的root路径
        String offsetZkRoot = "/" + topic;

        //存储该spout id的消费offset信息,譬如以topoName来命名
        String offsetZkId = UUID.randomUUID().toString();

        SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, topic, offsetZkRoot, offsetZkId);
        kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

        //kafka.api.OffsetRequest.EarliestTime(): 从topic 初始位置读取消息 (例如，从最老的那个消息开始)
        //kafka.api.OffsetRequest.LatestTime(): 从topic尾部开始读取消息 (例如，新写入topic的信息)
        kafkaConfig.startOffsetTime = kafka.api.OffsetRequest.LatestTime();
        KafkaSpout spout = new KafkaSpout(kafkaConfig);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("spout", spout);

        //2、处理kafka消息
        builder.setBolt("bolt", new KafkaConsumerBolt()).shuffleGrouping("spout");

        //3、本地模式运行我们的storm作业
        Config config = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("KafkaConsumerTopology", config, builder.createTopology());

    }
}

