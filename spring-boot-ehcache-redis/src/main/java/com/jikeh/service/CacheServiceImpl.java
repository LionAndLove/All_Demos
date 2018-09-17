package com.jikeh.service;

import com.jikeh.redis.RedisManager;
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
        return getAdFromRedis(adId);
    }

    /**
     * 调用redis：
     * @param adId
     * @return
     */
    public String getAdFromRedis(Long adId){
        RedisManager manager = RedisManager.getInstance();
        System.out.printf("调用redis获取数据");
        String key = "ad_info_" + adId;
        return manager.get(key);
    }

}
