package com.jikeh.redis.bolt;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;

public class PrintWordTotalCountBolt extends BaseRichBolt {
    private static final Logger LOG = LoggerFactory.getLogger(PrintWordTotalCountBolt.class);
    private static final Random RANDOM = new Random();
    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        String wordName = input.getStringByField("wordName");
        String countStr = input.getStringByField("count");

        // print lookup result with low probability
        if(RANDOM.nextInt(1000) > 995) {
            int count = 0;
            if (countStr != null) {
                count = Integer.parseInt(countStr);
            }
            LOG.info("Lookup result - word : " + wordName + " / count : " + count);
        }

        collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
