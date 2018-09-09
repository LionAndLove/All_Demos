package com.jikeh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Future;

/**
 * 缓存服务的接口：
 *
 * 背景：nginx开始，各级缓存都失效了，nginx发送很多的请求直接到缓存服务来获取最原始的数据
 * 	可能对系统的冲击比较大，为了避免冲垮整个广告系统，我们对于这个服务我们使用hystrix的线程池隔离技术
 *
 * 	http://localhost:8022/get/ads?adIds=1,1,3,3,5,5 是否读取缓存测试
 *
 * 	结合hystrix-dashboard进行测试：效果可能会更好
 * 	http://localhost:8080/hystrix-dashboard/
 * 	http://localhost:8022/hystrix.stream
 *
 * @author 极客慧 www.jikeh.cn
 *
 */
@Controller
public class CacheController {

	/**
	 * 获取单条广告数据：
	 *
	 * 1、http请求获取该广告的最新信息（对于这个服务我们使用hystrix的线程池隔离技术）
	 * 2、将广告信息刷入缓存中
	 *
	 * @param adId
	 * @return
	 */
	@RequestMapping("/get/ad")
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
	@RequestMapping("/get/ads")
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

		HystrixCommand<AdInfo> getAdInfoCommand = new GetAdInfoCommand(adId);

		//同步获取执行结果：
		AdInfo adInfo = getAdInfoCommand.execute();

		//异步获取执行结果
//		Future<AdInfo> future = getProductInfoCommand.queue();
//		try {
//			Thread.sleep(1000); 
//			System.out.println(future.get());  
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		return adInfo;
	}

	/**
	 * 批量获取广告数据：
	 *
	 * @param adIds 批量广告ids
	 * @return
	 */
	public List<AdInfo> getAdInfos(String adIds) {

		List<AdInfo> ads = new ArrayList<>();

		for(String adId : adIds.split(",")) {
			GetAdInfoCommand getAdInfoCommand = new GetAdInfoCommand(
					Long.valueOf(adId));
			AdInfo adInfo = getAdInfoCommand.execute();
			ads.add(adInfo);
			System.out.println("是否读取缓存："+getAdInfoCommand.isResponseFromCache());
		}

		return new ArrayList<>(ads);
	}
	
}
