package com.jikeh.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

public interface test {

/**
     * Called when a task for this component is initialized within a worker on the cluster.
     * It provides the spout with the environment in which the spout executes.
     *
     * This includes the:
     *
     * @param conf The Storm configuration for this spout. This is the configuration provided to the topology merged in with cluster configuration on this machine.
     * @param context This object can be used to get information about this task's place within the topology, including the task id and component id of this task, input and output information, etc.
     * @param collector The collector is used to emit tuples from this spout. Tuples can be emitted at any time, including the open and close methods. The collector is thread-safe and should be saved as an instance variable of this spout object.
     */
    void open(Map conf, TopologyContext context, SpoutOutputCollector collector);

    /**
     * When this method is called, Storm is requesting that the Spout emit tuples to the
     * output collector. This method should be non-blocking, so if the Spout has no tuples
     * to emit, this method should return. nextTuple, ack, and fail are all called in a tight
     * loop in a single thread in the spout task. When there are no tuples to emit, it is courteous
     * to have nextTuple sleep for a short amount of time (like a single millisecond)
     * so as not to waste too much CPU.
     */
    void nextTuple();

    /**
     * Declare the output schema for all the streams of this topology.
     *
     * @param declarer this is used to declare output stream ids, output fields, and whether or not each output stream is a direct stream
     */
    void declareOutputFields(OutputFieldsDeclarer declarer);

/**
 * Storm has determined that the tuple emitted by this spout with the msgId identifier
 * has been fully processed. Typically, an implementation of this method will take that
 * message off the queue and prevent it from being replayed.
 */
void ack(Object msgId);

/**
 * The tuple emitted by this spout with the msgId identifier has failed to be
 * fully processed. Typically, an implementation of this method will put that
 * message back on the queue to be replayed at a later time.
 */
void fail(Object msgId);





}
