package com.jikeh.service;

import com.jikeh.http.HttpClientUtils;
import com.jikeh.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 实时更新redis的数据：
 */
@Service
public class UpdateRedisServiceImpl {

    private Logger logger = LoggerFactory.getLogger(UpdateRedisServiceImpl.class);

    public boolean updateRedis(Long adId, String adStr){
        RedisManager manager = RedisManager.getInstance();
        if(StringUtils.isEmpty(adStr)){
            return false;
        }
        String key = "ad_info_" + adId;
        //刷入数据到redis：
        return manager.setStrWithExpire(key, adStr, 1);
    }

    /**
     * 调用db：
     * @param adId
     * @return
     */
    public String getAdFromDb(Long adId){
        String url = "http://localhost:1111/ad-manage/api/getAd?adId=" + adId;
        logger.info("调用接口获取广告数据, 接口 = {}", url);
        String res = HttpClientUtils.sendGetRequest(url);
        return res;
    }

    /**
     * 分布式存储UUID：
     *
     * @param uuid
     * @return
     */
    public boolean setUuid(String uuid){
        RedisManager manager = RedisManager.getInstance();
        return manager.setStrWithExpire(uuid, "1", 1);
    }

    public boolean isExist(String key){
        RedisManager manager = RedisManager.getInstance();
        return manager.exists(key);
    }

}
