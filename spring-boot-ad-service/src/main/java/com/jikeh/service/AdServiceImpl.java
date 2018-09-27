package com.jikeh.service;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.http.HttpClientUtils;
import com.jikeh.model.Ad;
import com.jikeh.redis.RedisManager;
import com.jikeh.zk.ZooKeeperSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@CacheConfig(cacheNames = "ads")
public class AdServiceImpl {

    private Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 数据为null的不缓存：
     * @param adId
     * @return
     */
    @Cacheable(value = "ads", key = "#p0", unless="#result == null")
    public String getAd(Long adId){
        //首次调用，才会打印这句话; 在缓存有效时间内，接下来的调用就不会再打印这句话了;
        System.out.printf("没有调用本地缓存\n");
        return getAdFromRedis(adId);
    }

    /**
     * 调用redis：
     * @param adId
     * @return
     */
    public String getAdFromRedis(Long adId){

        RedisManager manager = RedisManager.getInstance();
        logger.info("调用redis获取数据\n");
        String key = "ad_info_" + adId;
        String res = manager.get(key);

        //redis查询不到数据，访问db
        if(res == null){
            Ad ad = getAdFromDb(adId);
            if(ad == null){
                return null;
            }
            res = JSONObject.toJSONString(ad);

            //接下来需要将数据刷入redis：此时就有可能存在并发冲突的问题了

            //1、获取锁：瞬时redis被set了，然后你比较的其实是旧版本的数据了，就覆盖了==>从获取redis数据之前你就不能set数据了
            ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
            zkSession.acquireDistributedLock(adId);

            //2、从redis获取数据：旧数据
            String existAdStr = manager.get(key);
            Ad existAd = null;
            if(existAdStr != null) {
                existAd = JSONObject.parseObject(existAdStr, Ad.class);
            }

            //3、判断是否有数据：不为null，比较时间版本；为null直接set
            if(existAd != null) {
                // 比较当前数据的时间版本比已有数据的时间版本是新还是旧
                try {
                    Date date = sdf.parse(ad.getUpdateTime());
                    Date existedDate = sdf.parse(existAd.getUpdateTime());

                    if(date.before(existedDate)) {
                        logger.info("current date[" + ad.getUpdateTime() + "] is before existed date[" + existAd.getUpdateTime() + "]");
                        return existAdStr;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                logger.info("current date[" + ad.getUpdateTime() + "] is after existed date[" + existAd.getUpdateTime() + "]");
            } else {
                logger.info("existed ad info is null......");
            }

            try {
                Thread.sleep(30 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //4、刷入数据到redis：
            manager.setStrWithExpire(key, res, 1);

            //5、释放锁
            zkSession.releaseDistributedLock(adId);
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
        String url = "http://localhost:1111/ad-manage/api/getAd?adId=" + adId;
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, Ad.class);
    }

}
