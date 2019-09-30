package com.jikeh.performance.pool;

import org.apache.http.impl.client.CloseableHttpClient;


/**
 * @Author: wangdejin
 * @Description:
 * @Date: Created in 15:16 2018/11/21
 **/
public interface HttpClientService {
    /**
     * 获取HttpClient
     * @param config
     * @return
     */
    CloseableHttpClient httpClient(RequestRouteConfig config);

}
