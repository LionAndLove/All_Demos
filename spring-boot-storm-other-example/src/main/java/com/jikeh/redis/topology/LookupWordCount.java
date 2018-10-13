/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jikeh.redis.topology;

import com.jikeh.redis.mapper.WordCountRedisLookupMapper;
import com.jikeh.redis.bolt.PrintWordTotalCountBolt;
import com.jikeh.redis.spout.WordSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.redis.bolt.RedisLookupBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisLookupMapper;
import org.apache.storm.topology.TopologyBuilder;

public class LookupWordCount {
    private static final String WORD_SPOUT = "WORD_SPOUT";
    private static final String LOOKUP_BOLT = "LOOKUP_BOLT";
    private static final String PRINT_BOLT = "PRINT_BOLT";

    private static final String TEST_REDIS_HOST = "127.0.0.1";
    private static final int TEST_REDIS_PORT = 6379;



    public static void main(String[] args) {

        JedisPoolConfig poolConfig = new JedisPoolConfig.Builder().setHost(TEST_REDIS_HOST).setPort(TEST_REDIS_PORT).build();

        WordSpout spout = new WordSpout();

        RedisLookupMapper lookupMapper = setupLookupMapper();
        RedisLookupBolt lookupBolt = new RedisLookupBolt(poolConfig, lookupMapper);

        PrintWordTotalCountBolt printBolt = new PrintWordTotalCountBolt();

        //wordspout -> lookupbolt
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout(WORD_SPOUT, spout);
        builder.setBolt(LOOKUP_BOLT, lookupBolt).shuffleGrouping(WORD_SPOUT);
        builder.setBolt(PRINT_BOLT, printBolt).shuffleGrouping(LOOKUP_BOLT);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("LookupWordCount", new Config(), builder.createTopology());

    }

    private static RedisLookupMapper setupLookupMapper() {
        return new WordCountRedisLookupMapper();
    }

}
