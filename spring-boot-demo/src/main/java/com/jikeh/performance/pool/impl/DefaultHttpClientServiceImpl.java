package com.jikeh.performance.pool.impl;

import com.jikeh.performance.pool.HttpClientService;
import com.jikeh.performance.pool.RequestRouteConfig;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: wangdejin
 * @Description: Http连接池初始化和获取服务类
 * @Date: Created in 15:21 2018/11/21
 **/
@Component("httpClientService")
public class DefaultHttpClientServiceImpl implements HttpClientService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultHttpClientServiceImpl.class);

    private Map<String, CloseableHttpClient> httpClientMap = new ConcurrentHashMap<>();

    private static final String COMMON_HTTP_POOL = "1";
    private static final String MAX_TOTAL_PREFIX = "http.max.tcp.total.group.";
    private static final String MAX_ROUTE_PREFIX = "http.max.tcp.route.total.group.";
    private static final String TIME_PREFIX = "http.max.req.time.group.";
    private static final String SOCKET_TIME_PREFIX = "http.max.req.socket.time.group.";

    private static final String GROUP_NUM = "http.client.group.num";

    @Autowired
    protected Environment env;

    @Override
    public CloseableHttpClient httpClient(RequestRouteConfig config) {
        CloseableHttpClient client = httpClientMap.get(config.getHttpPoolCode());
        if (client == null) {
            logger.warn("httpPoolCode not match httpClient, config = {}", config);
            client = httpClientMap.get(COMMON_HTTP_POOL);
        }
        return client;
    }

    @PostConstruct
    private void init() {
        Integer poolGroupNum = env.getProperty(GROUP_NUM, Integer.class);
        for (int i = 1; i <= poolGroupNum; i++) {
            CloseableHttpClient client = initCloseableHttpClient(String.valueOf(i));
            httpClientMap.put(String.valueOf(i), client);
        }
    }

    private CloseableHttpClient initCloseableHttpClient(String httpPoolCode) {

        Integer maxTotal = env.getProperty(MAX_TOTAL_PREFIX + httpPoolCode, Integer.class);
        Integer MaxRoute = env.getProperty(MAX_ROUTE_PREFIX + httpPoolCode, Integer.class);
        Integer time = env.getProperty(TIME_PREFIX + httpPoolCode, Integer.class);
        Integer socketTime = env.getProperty(SOCKET_TIME_PREFIX + httpPoolCode, Integer.class);
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(time).setConnectionRequestTimeout(time)
                .setSocketTimeout(socketTime).build();
        //连接存活策略
        ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext
                    context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(
                        response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                            logger.error(ignore.getMessage(), ignore);
                        }
                    }
                }
                // otherwise keep alive for 3 seconds
                return 4500;
            }
        };
        httpClientConnectionManager.setMaxTotal(maxTotal);
        httpClientConnectionManager.setDefaultMaxPerRoute(MaxRoute);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(httpClientConnectionManager)
                .setKeepAliveStrategy(keepAliveStrategy)
                .setDefaultRequestConfig(requestConfig).disableAutomaticRetries().build();
        return httpClient;

    }

    public CloseableHttpClient getHttpClient(String key) {
        return httpClientMap.get(key);
    }

}
