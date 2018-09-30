package com.jikeh.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public class Sumbolt extends BaseRichBolt {

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
        //input.getInteger(0);
        Integer value = input.getIntegerByField("num");
        sum+=value;
        System.out.println("sum:"+sum);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
