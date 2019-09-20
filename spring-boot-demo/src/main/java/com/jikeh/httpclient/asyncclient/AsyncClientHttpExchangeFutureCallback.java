package com.jikeh.httpclient.asyncclient;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates a fully asynchronous execution of multiple HTTP exchanges
 * where the result of an individual operation is reported using a callback interface.
 */
public class AsyncClientHttpExchangeFutureCallback {

    private static CloseableHttpAsyncClient httpclient = null;

    static{
        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(500)
                    .setConnectTimeout(500)
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
        final HttpResponse[] httpResponse = new HttpResponse[1];
        try {
            final HttpGet request = new HttpGet("http://baidu.com/");
            Future<HttpResponse> responseFuture = httpclient.execute(request, new FutureCallback<HttpResponse>() {

                @Override
                public void completed(final HttpResponse response) {
                    System.out.println(request.getRequestLine() + "->" + response.getStatusLine());

                    //                        System.out.println(EntityUtils.toString(response.getEntity()));
                    httpResponse[0] = response;

                }

                @Override
                public void failed(final Exception ex) {

                }

                @Override
                public void cancelled() {
                    System.out.println(request.getRequestLine() + " cancelled");
                }

            });
            try{
                HttpResponse response = responseFuture.get();
//                System.out.println(EntityUtils.toString(response.getEntity()));
                HttpResponse temp = httpResponse[0];
                System.out.println(EntityUtils.toString(temp.getEntity()));
            }catch (Exception ex){
                if(ex instanceof TimeoutException){
                    System.out.println("连接超时");
                }else if(ex instanceof SocketTimeoutException){
                    System.out.println("传输超时");
                }else {
                    System.out.println("未知异常");
                }
//                e.printStackTrace();
            }
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

}
