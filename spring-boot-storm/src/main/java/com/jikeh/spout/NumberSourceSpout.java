package com.jikeh.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

public class NumberSourceSpout extends BaseRichSpout {

    private Map conf;
    private TopologyContext context;
    private SpoutOutputCollector collector;

    /**
     * 本实例运行的是被调用一次，只能执行一次。
     */
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.conf = conf;
        this.context = context;
        this.collector = collector;
    }

    /**
     * 死循环的调用，心跳
     */
    int i=0;
    public void nextTuple() {
        System.out.println("spout:"+i);
        this.collector.emit(new Values(i++));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 声明输出的内容
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("num"));
    }

}
