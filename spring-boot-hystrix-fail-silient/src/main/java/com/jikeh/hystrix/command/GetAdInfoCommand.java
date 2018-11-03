package com.jikeh.hystrix.command;

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
		// 如果调用失败了，报错了，那么就会去调用fallback降级机制
		throw new Exception();
	}

	@Override
	protected AdInfo getFallback() {
		return null;
	}

}
