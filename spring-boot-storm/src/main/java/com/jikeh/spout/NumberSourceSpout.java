package com.jikeh.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 数据源产生，并发射数据：
 */
public class NumberSourceSpout extends BaseRichSpout {

    private Map conf;
    private TopologyContext context;
    private SpoutOutputCollector collector;

    /**
     * 初始化方法，只会被调用一次，执行一次：
     * @param conf 配置参数
     * @param context 上下文（一个容器，装载各种东西）
     * @param collector 数据发射器
     */
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.conf = conf;
        this.context = context;
        this.collector = collector;
    }

    /**
     * 死循环的调用，不断地生产数据流，并发射出去
     */
    int i=0;
    public void nextTuple() {
        System.out.println("spout:"+i);

        //new Value(可变参数)：这里面也可以发送多个值(可以是多种类型)，这与下面的declareOutputFields是对应的，这边一个，下面就一个；这边是两个，下面也就是两个；
        this.collector.emit(new Values(i++));

        //这里让其sleep几秒，因为我是本地模式执行，不能让其发送数据太快，以免将我们的本机给拖死
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * new Fields(可变参数) 定义我们发送数据tuple的名称：因为下游要通过该名称来接收数据tuple
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

        //我们发送给bolt时，可以发送多个字段：这里面可以传递可变参数
        //因为我们上面就emit了一个值，所以我们这里就定义一个字段就可以了
        //如果不定义字段，下游bolt还可以通过下标来获取数据tuple
        declarer.declare(new Fields("num"));
    }

}
