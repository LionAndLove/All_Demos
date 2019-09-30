package com.jikeh.performance.pool;

/**
 * Created by calif on 2019/4/17.
 */
public abstract class RequestRouteConfig {

    /**
     * dsp id
     * @return
     */
    public abstract String getDspId();

    /**
     * 连接池编号
     * @return
     */
    public abstract String getHttpPoolCode();
}
