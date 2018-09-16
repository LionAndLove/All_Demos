package com.jikeh.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "ads")
public class CacheServiceImpl {

    @Cacheable(value = "ads", key = "#p0")
    public String getAd(Long adId){
        //首次调用，才会打印这句话; 在缓存有效时间内，接下来的调用就不会再打印这句话了;
        System.out.printf("没有执行缓存\n");
        return "{\"id\":"+adId+",\"name\":\"极客慧\",\"destination_url\":\"http://www.jikeh.cn\",\"price\":1000,\"img_url\":\"http://www.jikeh.cn/zb_users/upload/2018/08/201808101533915719235619.png\",\"startTime\":\"2018-08-25 12:00:00\",\"endTime\":\"2019-08-25 12:00:00\"}\n";
    }

}
