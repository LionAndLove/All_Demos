package com.jikeh;

import com.jikeh.redis.RedisManager;

public class JedisTest {
	
	public static void main(String[] args) {
        RedisManager manager = RedisManager.getInstance();
        Long adId = 1L;
        String str = "{\"id\":"+adId+",\"name\":\"极客慧\",\"destination_url\":\"http://www.jikeh.cn\",\"price\":1000,\"img_url\":\"http://www.jikeh.cn/zb_users/upload/2018/08/201808101533915719235619.png\",\"startTime\":\"2018-08-25 12:00:00\",\"endTime\":\"2019-08-25 12:00:00\"}\n";
        String key = "ad_info_" + adId;
        manager.setStr(key, str);
	}

}
