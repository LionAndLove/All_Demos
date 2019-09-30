package com.jikeh.httpclient.asyncclient;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.charset.CodingErrorAction;
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
                    .setSocketTimeout(100)
                    .setConnectTimeout(100)
                    .build();

            // Create I/O reactor configuration
            IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
                    .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                    .setConnectTimeout(1)
                    .setSoTimeout(1)
                    .setIoThreadCount(300)
                    .build();
            ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
            PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);

            MessageConstraints messageConstraints = MessageConstraints.custom()
                    .setMaxHeaderCount(200)
                    .setMaxLineLength(2000)
                    .build();
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setCharset(Consts.UTF_8)
                    .setMessageConstraints(messageConstraints)
                    .build();
            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(500);

            httpclient = HttpAsyncClients.custom().setConnectionManager(connManager)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            httpclient.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("未知异常");
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
                System.out.println("超时ConnectException异常");
            }else if (e instanceof ExecutionException) {
                System.out.println("超时ExecutionException异常");
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
