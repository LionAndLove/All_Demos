package com.jikeh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 广告服务的接口
 * @author 极客慧 www.jikeh.cn
 *
 */
@Controller
public class AdController {

	@RequestMapping("/getAdInfo")
	@ResponseBody
	public String getAdInfo(Long adId) {
		return "{\"id\":"+adId+",\"name\":\"极客慧\",\"destination_url\":\"http://www.jikeh.cn\",\"price\":1000,\"img_url\":\"http://www.jikeh.cn/zb_users/upload/2018/08/201808101533915719235619.png\",\"startTime\":\"2018-08-25 12:00:00\",\"endTime\":\"2019-08-25 12:00:00\"}\n";
	}
	
}
