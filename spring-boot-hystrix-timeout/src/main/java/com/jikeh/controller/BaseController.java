package com.jikeh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.hystrix.command.GetAdInfoCommand;
import com.jikeh.hystrix.command.GetAdInfosCommand;
import com.jikeh.model.AdInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 缓存服务的接口：
 *
 * @author 极客慧 www.jikeh.cn
 *
 */
@Controller
public class BaseController {

	/**
	 * 获取单条广告数据：
	 *
	 * @param adId
	 * @return
	 */
	@RequestMapping("/get/adInfo")
	@ResponseBody
	public String getAd(Long adId) {

		AdInfo response = getAdfo(adId);
		
		return JSON.toJSONString(response);
	}

	/**
	 * 批量获取广告数据：
	 *
	 * @param adIds
	 * @return
	 */
	@RequestMapping("/get/adsInfo")
	@ResponseBody
	public String getAds(String adIds) {

		 List<AdInfo> response = getAdInfos(adIds);

		return JSON.toJSONString(response);
	}

	/**
	 * 获取单条广告数据：
	 *
	 * @param adId 广告单ID
	 * @return
	 */
	private AdInfo getAdfo(Long adId) {

		String url = "http://127.0.0.1:1111/getAdInfo?adId=" + adId;
		String response = HttpClientUtils.sendGetRequest(url);
		return JSONObject.parseObject(response, AdInfo.class);

	}

	/**
	 * 批量获取广告数据：
	 *
	 * @param ids 批量广告ids
	 * @return
	 */
	public List<AdInfo> getAdInfos(String ids) {

		String[] adIds = ids.split(",");

		List<AdInfo> resAds = new ArrayList<>();
		for(String adId : adIds) {
			String url = "http://127.0.0.1:1111/getAdInfo?adId=" + adId;
			String response = HttpClientUtils.sendGetRequest(url);
			AdInfo adInfo = JSONObject.parseObject(response, AdInfo.class);
			resAds.add(adInfo);
		}

		return resAds;

	}
	
}
