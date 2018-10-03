package com.jikeh.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * 接收数据tuple，并处理
 */
public class NumberSumbolt extends BaseRichBolt {

    private Map stormConf;
    private TopologyContext context;
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.stormConf = stormConf;
        this.context = context;
        this.collector = collector;
    }

    int sum = 0;
    public void execute(Tuple input) {
        //input.getInteger(0);//通过下标来获取数据tuple
        Integer value = input.getIntegerByField("num");//通过名称来获取数据tuple
        sum += value;
        System.out.println("sum:"+sum);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
