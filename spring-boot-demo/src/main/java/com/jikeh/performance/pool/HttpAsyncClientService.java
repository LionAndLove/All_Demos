package com.jikeh.performance.pool;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;


/**
 * @Author: hanwenteng
 * @Description:
 * @Date: Created in 15:16 2019/09/29
 **/
public interface HttpAsyncClientService {
    /**
     * 获取HttpAsynClient
     * @param config
     * @return
     */
    CloseableHttpAsyncClient httpClient(RequestRouteConfig config);

    /**
     * 获取传输时间：
     * @param httpPoolCode
     * @return
     */
    int getSocketTime(String httpPoolCode);

}
