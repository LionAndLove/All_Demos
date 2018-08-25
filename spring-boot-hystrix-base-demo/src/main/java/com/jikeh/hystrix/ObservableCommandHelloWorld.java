package com.jikeh.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Hystrix基础实例2：批量请求(基于观察者)
 *
 * @author 极客慧 www.jikeh.cn
 */
public class ObservableCommandHelloWorld extends HystrixObservableCommand<String> {

    private String[] adIds;

    public ObservableCommandHelloWorld(String[] adIds) {
		super(HystrixCommandGroupKey.Factory.asKey("GetAdsGroup"));
        this.adIds = adIds;
    }

    @Override
    protected Observable<String> construct() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            public void call(Subscriber<? super String> observer) {
                try {
                    for(String adId : adIds) {
                        observer.onNext("Hello " + adId + "!");
                    }
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }

        }).subscribeOn(Schedulers.io());
    }

}
