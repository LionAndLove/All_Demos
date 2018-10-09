package com.jikeh.spout;

import org.apache.commons.io.FileUtils;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class WordSourceSpout extends BaseRichSpout {

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
    public void nextTuple() {
        //读取指定目录下所有文件
        Collection<File> files = FileUtils.listFiles(new File("/usr/local/src/storm_test"), new String[]{"txt"}, true);
        for (File file : files) {
            try {
                //获取每个文件的所有数据
                List<String> lines = FileUtils.readLines(file);
                //把每一行数据发射出去
                for (String line : lines) {
                    this.collector.emit(new Values(line));
                }
                FileUtils.moveFile(file, new File(file.getAbsolutePath()+System.currentTimeMillis()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 声明输出的内容
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));
    }

}
