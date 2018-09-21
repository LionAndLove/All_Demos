package com.jikeh.controller;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.model.Ad;
import com.jikeh.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api")
public class ApiController {

    @Autowired
    AdService adService;

    /**
     * 全量获取数据：
     *
     * @return
     */
    @RequestMapping(value = "getAds", method = RequestMethod.GET)
    @ResponseBody
    public String getAds(){
        List<Ad> ad = adService.getAds();
        return JSONObject.toJSONString(ad);
    }

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
