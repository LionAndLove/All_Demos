package com.jikeh.performance.pool;

import com.jikeh.performance.pool.impl.DefaultAsyncHttpClientServiceImpl;
import com.jikeh.performance.pool.impl.DefaultHttpClientServiceImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by hanwenteng on 2019/09/30.
 */
@Service
public class ServerActiveServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ServerActiveServiceImpl.class);
    private static final String numStr = "6";
    private static CloseableHttpClient httpClient = null;
    private static CloseableHttpAsyncClient httpAsyncClient = null;

    private static final String TIME_PREFIX = "http.max.req.time.group.";
    private static final String SOCKET_TIME_PREFIX = "http.max.req.socket.time.group.";

    @Autowired
    protected Environment env;

    @Resource(name = "httpClientService")
    private DefaultHttpClientServiceImpl httpClientService;

    @Resource(name = "httpAsynClientService")
    private DefaultAsyncHttpClientServiceImpl httpAsynClientService;

    @PostConstruct
    private void init() {
        httpClient = httpClientService.getHttpClient(numStr);
        httpAsyncClient = httpAsynClientService.getHttpClient(numStr);
    }

    /**
     * 同步client发送http get请求：
     *
     * @param url
     */
    public void sendHttpGetBySync(String url){
        URIBuilder uriBuilder = null;
        URI uri = null;
        HttpGet httpRequest = null;

        CloseableHttpResponse httpResponse = null;
        try {
            uriBuilder = new URIBuilder(url);
            uri = uriBuilder.build();
            httpRequest = new HttpGet(uri);
            httpResponse = httpClient.execute(httpRequest);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                HttpEntity entity = httpResponse.getEntity();                ;
                logger.info("响应信息，url = {}, resp = {}", url, EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            logger.error("暖身异常", e);
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
        }
    }

    /**
     * 异步client发送http get请求：
     *
     * @param url
     */
    public void sendHttpGetByAsync(String url){
        httpAsyncClient.start();

        URIBuilder uriBuilder = null;
        URI uri = null;
        HttpGet httpRequest = null;
        BasicHttpResponse httpResponse = null;
        try {
            uriBuilder = new URIBuilder(url);
            uri = uriBuilder.build();
            httpRequest = new HttpGet(uri);
            Future<HttpResponse> responseFuture = httpAsyncClient.execute(httpRequest, new RespCallBack());
            Integer connTime = env.getProperty(TIME_PREFIX + numStr, Integer.class);
            Integer socketTime = env.getProperty(SOCKET_TIME_PREFIX + numStr, Integer.class);
            httpResponse = (BasicHttpResponse) responseFuture.get(connTime + socketTime, TimeUnit.MILLISECONDS);
            if (HttpStatus.SC_OK == httpResponse.getStatusLine().getStatusCode()) {
                HttpEntity entity = httpResponse.getEntity();                ;
                logger.info("响应信息，url = {}, resp = {}", url, EntityUtils.toString(entity));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("未知异常", e);
        } finally {
            HttpClientUtils.closeQuietly(httpResponse);
        }
    }

    class RespCallBack implements FutureCallback<HttpResponse> {

        @Override
        public void completed(HttpResponse response) {
//            HttpClientUtils.closeQuietly(response);
        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    }

}
