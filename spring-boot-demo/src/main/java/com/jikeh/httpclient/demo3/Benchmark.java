package com.jikeh.httpclient.demo3;

/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

import java.net.URI;

/**
 * 测试结论：
 * 1、单线程
 * 2、线程数 >> CPU核数
 * 3、线程数 << CPU核数
 */
public class Benchmark {

    public static void main(final String[] args) throws Exception {
        final Config config = BenchRunner.parseConfig(args);
        config.setUri(new URI("https://www.163.com"));
        if (config.getUri() == null) {
            System.err.println("Please specify a target URI");
            System.exit(-1);
        }
        System.out.println("Running benchmark against " + config.getUri());
        BenchRunner.run(new ApacheHttpClient(), config);
        BenchRunner.run(new NingHttpClient(), config);
    }

}
