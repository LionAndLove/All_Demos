package com.jikeh.service;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.model.Ad;
import com.jikeh.redis.RedisManager;
import com.jikeh.zk.ZooKeeperSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 实时更新redis的数据：
 */
@Service
public class UpdateRedisServiceImpl {

    private Logger logger = LoggerFactory.getLogger(UpdateRedisServiceImpl.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public boolean updateRedis(Long adId, String adStr){

        Ad ad = null;
        if(StringUtils.isEmpty(adStr)){
            return false;
        }else {
            ad = JSONObject.parseObject(adStr, Ad.class);
        }

        RedisManager manager = RedisManager.getInstance();

        //1、获取锁
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
        zkSession.acquireDistributedLock(adId);

        //2、从redis获取数据
        String key = "ad_info_" + adId;
        String existAdStr = manager.get(key);
        Ad existAd = null;
        if(existAdStr != null) {
            existAd = JSONObject.parseObject(existAdStr, Ad.class);
        }

        //3、比较版本
        if(existAd != null) {
            // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
            try {
                Date date = sdf.parse(ad.getUpdateTime());
                Date existedDate = sdf.parse(existAd.getUpdateTime());

                if(date.before(existedDate)) {
                    logger.info("current date[" + ad.getUpdateTime() + "] is before existed date[" + existAd.getUpdateTime() + "]");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            logger.info("current date[" + ad.getUpdateTime() + "] is after existed date[" + existAd.getUpdateTime() + "]");
        } else {
            logger.info("existed product info is null......");
        }

        //4、set数据
        boolean flag = manager.setStrWithExpire(key, adStr, 1);

        //5、释放锁
        zkSession.releaseDistributedLock(adId);

        //刷入数据到redis：
        return flag;
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
