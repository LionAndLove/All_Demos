package com.jikeh.local;

import java.util.HashMap;
import java.util.Map;

/**
 * 广告本地缓存：
 * @author 极客慧 www.jikeh.cn
 *
 */
public class AdCache {

	private static Map<Long, String> adMap = new HashMap<Long, String>();
	
	static {
		adMap.put(1L, "{\"id\":\"1\",\"name\":\"极客慧\",\"destination_url\":\"http://www.jikeh.cn\",\"price\":1000,\"img_url\":\"http://www.jikeh.cn/zb_users/upload/2018/08/201808101533915719235619.png\",\"startTime\":\"2018-08-25 12:00:00\",\"endTime\":\"2019-08-25 12:00:00\"}\n");
		adMap.put(-1L, "{\"id\":\"-1\",\"name\":\"极客慧\",\"destination_url\":\"http://www.jikeh.cn\",\"price\":1000,\"img_url\":\"http://www.jikeh.cn/zb_users/upload/2018/08/201808101533915719235619.png\",\"startTime\":\"2018-08-25 12:00:00\",\"endTime\":\"2019-08-25 12:00:00\"}\n");
		adMap.put(-2L, "{\"id\":\"-2\",\"name\":\"极客慧\",\"destination_url\":\"http://www.jikeh.cn\",\"price\":1000,\"img_url\":\"http://www.jikeh.cn/zb_users/upload/2018/08/201808101533915719235619.png\",\"startTime\":\"2018-08-25 12:00:00\",\"endTime\":\"2019-08-25 12:00:00\"}\n");
	}
	
	public static String getAdInfo(Long adId) {
		return adMap.get(adId);
	}
	
}
