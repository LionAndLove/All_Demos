package com.jikeh.redis.mapper;

import com.google.common.collect.Lists;
import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisLookupMapper;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.ITuple;
import org.apache.storm.tuple.Values;

import java.util.List;

public class WordCountRedisLookupMapper implements RedisLookupMapper {
    private RedisDataTypeDescription description;
    private final String hashKey = "wordCount";

    public WordCountRedisLookupMapper() {
        description = new RedisDataTypeDescription(
                RedisDataTypeDescription.RedisDataType.HASH, hashKey);
    }

    /**
     * 处理redis中查询返回来的数据
     * @param input 上游tuple
     * @param value redis中返回的数据
     * @return
     */
    @Override
    public List<Values> toTuple(ITuple input, Object value) {
        String member = getKeyFromTuple(input);
        List<Values> values = Lists.newArrayList();

        //你可以加入一些对value的处理，然后再往下游发送数据
        //构建往下游发送数据的格式
        values.add(new Values(member, value));
        return values;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("wordName", "count"));
    }

    @Override
    public RedisDataTypeDescription getDataTypeDescription() {
        return description;
    }

    /**
     * 获取上游tuple的数据：
     * @param tuple
     * @return
     */
    @Override
    public String getKeyFromTuple(ITuple tuple) {
        return tuple.getStringByField("word");
    }

    @Override
    public String getValueFromTuple(ITuple tuple) {
        return null;
    }
}
