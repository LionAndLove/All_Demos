package com.luo.dubbo.util;

import org.apache.log4j.Logger;

public class Log {
    private static Logger log = Logger.getLogger(Log.class);

    public static void e(String message) {
        log.error(message);
    }

    public static void i(String message) {
        log.info(message);
    }

    public static void d(String message) {
        log.debug(message);
    }
}
