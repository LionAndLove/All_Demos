package com.jikeh.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

public class WordCountbolt extends BaseRichBolt {

    private Map stormConf;
    private TopologyContext context;
    private OutputCollector collector;

    public void prepare(Map stormConf, TopologyContext context,
                        OutputCollector collector) {
        this.stormConf = stormConf;
        this.context = context;
        this.collector = collector;
    }

    HashMap<String, Integer> hashMap = new HashMap<>();
    public void execute(Tuple input) {

        //获取每一个单词
        String word = input.getStringByField("words");

        //对所有的单词进行汇总
        Integer value = hashMap.get(word);
        if(value==null){
            value = 0;
        }

        value++;
        hashMap.put(word, value);

        //将每次统计结果打印出来：
        System.out.println("==================================");
        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            System.out.println(entry);
        }

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
