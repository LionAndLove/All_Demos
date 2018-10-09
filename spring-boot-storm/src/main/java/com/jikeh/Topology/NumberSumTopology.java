package com.jikeh.Topology;

import com.jikeh.bolt.NumberSumbolt;
import com.jikeh.spout.NumberSourceSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
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
        //按照拓扑图来实现我们的代码
        //Topology需要指定Spout、Bolt的执行顺序
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("number_spout_id", new NumberSourceSpout());
        //上下游通过什么来建立关系呢，要建立什么样的关系呢，即：通过什么方式来交互数据呢
        //通过分组策略，来指定我们接收上游数据的方式，上游发送数据的方式：随机发送、按字段名发送
        topologyBuilder.setBolt("sum_bolt_id", new NumberSumbolt()).shuffleGrouping("number_spout_id");

        // 代码提交到本地模式上运行：
//        LocalCluster localCluster = new LocalCluster();
//        localCluster.submitTopology("sum_topology", new Config(), topologyBuilder.createTopology());

        // 代码提交到Storm集群上运行：
        String topoName = NumberSumTopology.class.getSimpleName();
        try {
            StormSubmitter.submitTopology(topoName, new Config(), topologyBuilder.createTopology());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
