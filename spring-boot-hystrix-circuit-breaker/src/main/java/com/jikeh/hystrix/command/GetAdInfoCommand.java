package com.jikeh.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.local.AdCache;
import com.jikeh.model.AdInfo;
import com.netflix.hystrix.*;

import java.util.concurrent.TimeoutException;

/**
 * 获取广告信息
 * @author 极客慧 www.jikeh.cn
 *
 */
public class GetAdInfoCommand extends HystrixCommand<AdInfo> {

	private Long adId;

	private static int count = 1;
	
	public GetAdInfoCommand(Long adId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetAdInfoGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("GetAdInfoCommand"))
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetAdInfoPool"))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
						.withCoreSize(15)
						.withQueueSizeRejectionThreshold(10))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withFallbackIsolationSemaphoreMaxConcurrentRequests(15)
						//默认滑动窗口大小：10，注意这里设置的窗口大小一定要 >= withCircuitBreakerRequestVolumeThreshold，否则断路器始终open不了
						.withMetricsRollingStatisticalWindowBuckets(2)
						//默认滑动窗口时间窗：10s
						.withMetricsRollingStatisticalWindowInMilliseconds(6000)
						//滑动窗口时间窗内，经过短路器的流量超过了一定的阈值，才执行短路，注意是滑动窗口内
						//4s时间内经过断路器的流量 >= 2，且错误比例>40%，断路器状态才会被打开
						.withCircuitBreakerRequestVolumeThreshold(2)
						//断路器统计到的异常调用的占比超过了一定的阈值，才执行短路，默认是50%
						.withCircuitBreakerErrorThresholdPercentage(40)
						//half-open状态：试探服务
						.withCircuitBreakerSleepWindowInMilliseconds(6000))
		);
		this.adId = adId;
	}
	
	@Override
	protected AdInfo run() throws Exception {

		//如果断路器打开了，是打印不了这句话的
		System.out.println("第"+count+"次调用接口，开始查询广告数据，adId=" + adId);
		count++;

		if(adId.equals(-1L)) {
			// 如果调用失败了，报错了，那么就会去调用fallback降级机制
			throw new Exception();
		}

		String url = "http://127.0.0.1:8011/getAdInfo?adId=" + adId;
		String response = HttpClientUtils.sendGetRequest(url);
		System.out.println("调用接口，成功查询广告数据，res=" + response);
		return JSONObject.parseObject(response, AdInfo.class);

	}

	@Override
	protected AdInfo getFallback() {
		System.out.println("降级：从本地缓存获取过期的广告数据，adId=" + adId);
		return JSONObject.parseObject(AdCache.getAdInfo(adId), AdInfo.class);
	}

//	@Override
//	protected String getCacheKey() {
//		return "ad_info_" + adId;
//	}

}
