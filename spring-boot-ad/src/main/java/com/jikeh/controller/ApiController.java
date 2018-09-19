package com.jikeh.controller;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.model.Ad;
import com.jikeh.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    AdService adService;

    /**
     * 对外提供查询广告的接口：http://localhost:1111/ad-manage/api/getAd?adId=1
     * @param adId
     * @return
     */
    @RequestMapping(value = "getAd", method = RequestMethod.GET)
    @ResponseBody
    public String getAd(Long adId){
        Ad ad = adService.findById(adId);
        return JSONObject.toJSONString(ad);
    }

}
