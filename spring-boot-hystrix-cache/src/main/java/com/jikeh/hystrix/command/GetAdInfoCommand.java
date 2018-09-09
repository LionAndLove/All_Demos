package com.jikeh.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.model.AdInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 获取广告信息
 * @author 极客慧 www.jikeh.cn
 *
 */
public class GetAdInfoCommand extends HystrixCommand<AdInfo> {

	private Long adId;
	
	public GetAdInfoCommand(Long adId) {
		super(HystrixCommandGroupKey.Factory.asKey("GetAdInfoGroup"));
		this.adId = adId;
	}
	
	@Override
	protected AdInfo run() {
		String url = "http://127.0.0.1:8011/getAdInfo?adId=" + adId;
		String response = HttpClientUtils.sendGetRequest(url);
		System.out.println("调用接口，查询广告数据，adId=" + adId);
		return JSONObject.parseObject(response, AdInfo.class);
	}

	@Override
	protected String getCacheKey() {
		return "ad_info_" + adId;
	}

}
