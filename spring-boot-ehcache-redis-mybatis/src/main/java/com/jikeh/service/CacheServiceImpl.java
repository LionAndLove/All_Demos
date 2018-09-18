package com.jikeh.service;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.mapper.AdMapper;
import com.jikeh.model.Ad;
import com.jikeh.redis.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "ads")
public class CacheServiceImpl {

    @Autowired
    private AdMapper adMapper;

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
        System.out.printf("调用redis获取数据\n");
        String key = "ad_info_" + adId;
        String res = manager.get(key);
        if(res == null){
            //redis查询不到数据，访问db
            // TODO: 2018/9/18 0018 当并发量太高的时候，会有大量的并发同时查询db，即：缓存穿透、缓存雪崩，后期我们会对这里进行优化
            Ad ad = getAdFromDb(adId);
            res = JSONObject.toJSONString(ad);

            //刷入数据到redis：
            manager.setStr(key, res);
        }
        return res;
    }

    /**
     * 调用db：
     * @param adId
     * @return
     */
    public Ad getAdFromDb(Long adId){
        System.out.printf("调用db获取数据");
        return adMapper.selectByPrimaryKey(adId);
    }

}
