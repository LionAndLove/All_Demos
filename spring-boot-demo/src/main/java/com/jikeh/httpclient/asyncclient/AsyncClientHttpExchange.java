package com.jikeh.httpclient.asyncclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This example demonstrates a basic asynchronous HTTP request / response exchange.
 * Response content is buffered in memory for simplicity.
 */
public class AsyncClientHttpExchange {

    private static CloseableHttpAsyncClient httpclient = null;

    static {
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(1)
                    .setConnectTimeout(1)
                    .build();

            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
            connManager.setMaxTotal(500);
            connManager.setDefaultMaxPerRoute(500);

            httpclient = HttpAsyncClients.custom().setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            httpclient.start();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void main(final String[] args) throws Exception {
        try {
            HttpGet request = new HttpGet("http://localhost");
            Future<HttpResponse> future = httpclient.execute(request, null);
            HttpResponse response = future.get(60, TimeUnit.SECONDS);
            System.out.println("Response: " + response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("Shutting down");
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                System.out.println("连接超时异常");
            }else if (e instanceof ExecutionException) {
                System.out.println("连接超时异常");
            }  else if (e instanceof TimeoutException) {
                System.out.println("超时异常");
            } else if (e instanceof SocketTimeoutException) {
                System.out.println("传输异常");
            } else {
                e.printStackTrace();
                System.out.println("未知异常");
            }
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

}
