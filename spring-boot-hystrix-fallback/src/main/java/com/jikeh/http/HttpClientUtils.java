package com.jikeh.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * HttpClient工具类
 * @author 极客慧 www.jikeh.cn
 *
 */
@SuppressWarnings("deprecation")
public class HttpClientUtils {
	
	/**
	 * 发送GET请求
	 * @param url 请求URL
	 * @return 响应结果
	 */
	@SuppressWarnings("resource")
	public static String sendGetRequest(String url) {

		String resp = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse httpResponse = null;
		
		try {
			// 发送GET请求
			HttpGet httpget = new HttpGet(url);  
			httpResponse = httpclient.execute(httpget);
			
			// 处理响应
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				resp = EntityUtils.toString(entity);
			}

		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {
			if (httpResponse != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity()); //会自动释放连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		  
		return resp;
	}
	
}