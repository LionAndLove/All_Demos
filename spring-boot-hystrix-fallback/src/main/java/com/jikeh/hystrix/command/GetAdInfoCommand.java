package com.jikeh.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.local.AdCache;
import com.jikeh.model.AdInfo;
import com.netflix.hystrix.*;

/**
 * 获取广告信息
 * @author 极客慧 www.jikeh.cn
 *
 */
public class GetAdInfoCommand extends HystrixCommand<AdInfo> {

	private Long adId;
	
	public GetAdInfoCommand(Long adId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetAdInfoGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetAdInfoCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetAdInfoPool"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(15)
						.withQueueSizeRejectionThreshold(10))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(15))
		);
		this.adId = adId;
	}
	
	@Override
	protected AdInfo run() throws Exception {
//		String url = "http://127.0.0.1:8011/getAdInfo?adId=" + adId;
//		String response = HttpClientUtils.sendGetRequest(url);
//		System.out.println("调用接口，查询广告数据，adId=" + adId);
//		return JSONObject.parseObject(response, AdInfo.class);

		// 如果调用失败了，报错了，那么就会去调用fallback降级机制
		throw new Exception();

	}

	@Override
	protected AdInfo getFallback() {
		System.out.println("从本地缓存获取过期的广告数据，adId=" + adId);
		return JSONObject.parseObject(AdCache.getAdInfo(adId), AdInfo.class);
	}

//	@Override
//	protected String getCacheKey() {
//		return "ad_info_" + adId;
//	}

}
