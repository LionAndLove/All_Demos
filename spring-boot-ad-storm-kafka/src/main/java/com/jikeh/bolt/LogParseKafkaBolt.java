package com.jikeh.bolt;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.domain.Message;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
public class LogParseKafkaBolt extends BaseRichBolt {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogParseKafkaBolt.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            String message = tuple.getStringByField("str");

            LOGGER.info("【LogParseBolt接收到一条日志】message=" + message);

//        String message = tuple.getString(0);

            Message messageObj = JSONObject.parseObject(message, Message.class);
            Long adId = messageObj.getId();
            if(adId != null) {
                collector.emit(new Values(adId));
                LOGGER.info("【LogParseBolt发射出去一个商品id】adId=" + adId);
            }

            this.collector.ack(tuple);

        } catch (Exception e) {
            this.collector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("adId"));
    }
}
