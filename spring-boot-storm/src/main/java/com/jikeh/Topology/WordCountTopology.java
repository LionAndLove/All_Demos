package com.jikeh.Topology;

import com.jikeh.bolt.WordCountbolt;
import com.jikeh.bolt.WordSplitbolt;
import com.jikeh.spout.WordSourceSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * 单词计数：
 *
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
public class WordCountTopology {

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("word_spout_id", new WordSourceSpout());
        topologyBuilder.setBolt("split_bolt_id", new WordSplitbolt()).shuffleGrouping("word_spout_id");
        topologyBuilder.setBolt("count_bolt_id", new WordCountbolt()).shuffleGrouping("split_bolt_id");

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("topology", new Config(), topologyBuilder.createTopology());
    }

}
