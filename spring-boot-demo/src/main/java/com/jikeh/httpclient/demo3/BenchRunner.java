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

import org.apache.commons.cli.*;

import java.net.URI;
import java.net.URISyntaxException;

public class BenchRunner {

    public static Config parseConfig(final String[] args) throws ParseException {
        final Config config = new Config();
        if (args.length > 0) {
            final Option kopt = new Option("k", false, "Enable the HTTP KeepAlive feature, " +
                    "i.e., perform multiple requests within one HTTP session. " +
                    "Default is no KeepAlive");
            kopt.setRequired(false);
            final Option copt = new Option("c", true, "Concurrency while performing the " +
                    "benchmarking session. The default is to just use a single thread/client");
            copt.setRequired(false);
            copt.setArgName("concurrency");

            final Option nopt = new Option("n", true, "Number of requests to perform for the " +
                    "benchmarking session. The default is to just perform a single " +
                    "request which usually leads to non-representative benchmarking " +
                    "results");
            nopt.setRequired(false);
            nopt.setArgName("requests");

            final Option lopt = new Option("l", true, "Request content length");
            nopt.setRequired(false);
            nopt.setArgName("content length");

            final Options options = new Options();
            options.addOption(kopt);
            options.addOption(nopt);
            options.addOption(copt);
            options.addOption(lopt);

            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption('h')) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Benchmark [options]", options);
                System.exit(1);
            }
            if (cmd.hasOption('k')) {
                config.setKeepAlive(true);
            }
            if (cmd.hasOption('c')) {
                final String s = cmd.getOptionValue('c');
                try {
                    config.setConcurrency(Integer.parseInt(s));
                } catch (final NumberFormatException ex) {
                    System.err.println("Invalid number for concurrency: " + s);
                    System.exit(-1);
                }
            }
            if (cmd.hasOption('n')) {
                final String s = cmd.getOptionValue('n');
                try {
                    config.setRequests(Integer.parseInt(s));
                } catch (final NumberFormatException ex) {
                    System.err.println("Invalid number of requests: " + s);
                    System.exit(-1);
                }
            }
            if (cmd.hasOption('l')) {
                final String s = cmd.getOptionValue('l');
                try {
                    config.setContentLength(Integer.parseInt(s));
                } catch (final NumberFormatException ex) {
                    System.err.println("Invalid content length: " + s);
                    System.exit(-1);
                }
            }
            final String[] cmdargs = cmd.getArgs();
            if (cmdargs.length > 0) {
                try {
                    config.setUri(new URI(cmdargs[0]));
                } catch (final URISyntaxException ex) {
                    System.err.println("Invalid request URL : " + cmdargs[0]);
                    System.exit(-1);
                }
            }

        } else {
            config.setKeepAlive(true);
            config.setRequests(10);
            config.setConcurrency(1);
        }
        return config;
    }

    public static void run(final HttpAgent agent, final Config config) throws Exception {

        final byte[] content = new byte[config.getContentLength()];
        final int r = Math.abs(content.hashCode());
        for (int i = 0; i < content.length; i++) {
            content[i] = (byte) ((r + i) % 96 + 32);
        }

        final URI warmup = config.getUri();
        final URI target = config.getUri();

        try {
            agent.init();
            // Warm up
            agent.get(warmup, 500, 2);
            // Sleep a little
//            Thread.sleep(5000);

            System.out.println("=================================");
            System.out.println("HTTP agent: " + agent.getClientName());
            System.out.println("---------------------------------");
            System.out.println(config.getRequests() + " POST requests");
            System.out.println("---------------------------------");

            final long startTime = System.currentTimeMillis();
            final Stats stats = agent.post(target, content, config.getRequests(), config.getConcurrency());
            final long finishTime = System.currentTimeMillis();
            Stats.printStats(target, startTime, finishTime, stats);
        } finally {
            agent.shutdown();
        }
        System.out.println("---------------------------------");
    }

}