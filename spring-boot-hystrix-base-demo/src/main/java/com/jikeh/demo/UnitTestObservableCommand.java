package com.jikeh.demo;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.hystrix.ObservableCommandHelloWorld;
import com.netflix.hystrix.HystrixObservableCommand;
import org.junit.Test;
import rx.Observable;
import rx.Observer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Hystrix基础实例2：批量请求(基于观察者)
 */
public class UnitTestObservableCommand {

    //异步：
    @Test
    public void testAsynchronous() throws Exception {

        String adIds = "1,2,3";

        //异步获取执行：
        HystrixObservableCommand<String> getAdInfosCommand = new ObservableCommandHelloWorld(adIds.split(","));
        Observable<String> observable = getAdInfosCommand.observe();

        //订阅一个观察者来观察执行结果
        observable.subscribe(new Observer<String>() {

            public void onCompleted() {
                System.out.println("获取完了所有的广告数据");
            }

            public void onError(Throwable e) {
                e.printStackTrace();
            }

            public void onNext(String adInfo) {
                System.out.println(JSONObject.toJSONString(adInfo));
            }

        });

        //同步获取执行结果：我们一次查询，肯定是返回结果集的组合
        List<String> ads = new ArrayList<>();
        Iterator<String> iterator = observable.toBlocking().getIterator();
        while(iterator.hasNext()) {
            ads.add(iterator.next());
        }

        System.out.println("最终的结果集："+ads);

    }

}
