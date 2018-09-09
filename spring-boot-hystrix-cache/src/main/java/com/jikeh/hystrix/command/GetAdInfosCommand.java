package com.jikeh.hystrix.command;

import com.jikeh.http.HttpClientUtils;
import com.jikeh.model.AdInfo;
import com.netflix.hystrix.HystrixCommandProperties;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;

/**
 * 批量查询多个广告数据的command
 * @author 极客慧 www.jikeh.cn
 *
 */
public class GetAdInfosCommand extends HystrixObservableCommand<AdInfo> {

	private String[] adIds;
	
	public GetAdInfosCommand(String[] adIds) {
//		super(HystrixCommandGroupKey.Factory.asKey("GetAdInfoGroup"));
		super(Setter
				.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetAdInfoGroup"))
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(3000)));
		this.adIds = adIds;
	}
	
	@Override
	protected Observable<AdInfo> construct() {
		return Observable.create(new Observable.OnSubscribe<AdInfo>() {

			public void call(Subscriber<? super AdInfo> observer) {
				try {
					for(String adId : adIds) {
						String url = "http://127.0.0.1:8011/getAdInfo?adId=" + adId;
						String response = HttpClientUtils.sendGetRequest(url);
						AdInfo adInfo = JSONObject.parseObject(response, AdInfo.class);
						observer.onNext(adInfo);
					}
					observer.onCompleted();
				} catch (Exception e) {
					observer.onError(e);  
				}
			}
			
		}).subscribeOn(Schedulers.io());
	}

}
