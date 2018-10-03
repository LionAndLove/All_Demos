package com.jikeh.Topology;

import com.jikeh.bolt.NumberSumbolt;
import com.jikeh.spout.NumberSourceSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * 自然数求和：1+2+……n
 *
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
public class NumberSumTopology {

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("number_spout_id", new NumberSourceSpout());
        topologyBuilder.setBolt("sum_bolt_id", new NumberSumbolt()).shuffleGrouping("number_spout_id");

        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("sum_topology", new Config(), topologyBuilder.createTopology());
    }

}
