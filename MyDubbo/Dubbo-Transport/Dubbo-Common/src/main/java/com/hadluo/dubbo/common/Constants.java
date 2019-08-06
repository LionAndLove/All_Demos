package com.hadluo.dubbo.common;

public class Constants {

    /** netty 最大连接数 URL key */
    public static final String ACCEPTS_KEY = "accepts";

    /** netty最大传输 量 URL key */
    public static final String PAYLOAD_KEY = "payload";
    public static final String LINE_DELIMITER_KEY = "payload";
    /** netty编解码 传输 分隔符 */
    public static final String LINE_DELIMITER = "$#$";

    public static final int DEFAULT_ACCEPTS = 0;

    public static final int DEFAULT_PAYLOAD = 8 * 1024 * 1024; // 8M

    /** 心跳 时 ，上一次读 的时间搓 key */
    public static final String KEY_READ_TIMESTAMP = "key_read_timestamp";

    public static final String HEARTBEAT_KEY = "heartbeat";
    public static final String HEARTBEAT_TIMEOUT_KEY = "heartbeat.timeout";
    /** 默认心跳时间 */
    public static final int DEFAULT_HEARTBEAT = 5000; //
}
