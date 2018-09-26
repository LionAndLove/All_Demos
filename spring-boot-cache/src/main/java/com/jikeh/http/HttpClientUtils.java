package com.jikeh.http;

import com.jikeh.mq.Receiver;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HttpClient工具类
 * @author 极客慧 www.jikeh.cn
 *
 */
// TODO: 2018/9/20 0020 这个工具类 我们后期会进行优化 并使用该工具类对项目进行压测
@SuppressWarnings("deprecation")
public class HttpClientUtils {

	private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
	
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
			logger.error("http 请求失败", e);
		} finally {
			if (httpResponse != null) {
				try {
					EntityUtils.consume(httpResponse.getEntity()); //会自动释放连接
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return resp;
		}
	}
	
}