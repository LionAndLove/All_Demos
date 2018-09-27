package com.jikeh.controller;

import com.alibaba.fastjson.JSONObject;
import com.jikeh.model.Ad;
import com.jikeh.model.AdMessage;
import com.jikeh.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/ads")
public class AdController {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    AdService adService;

    /**
     * 显示字典数据列表：http://localhost:1111/ad-manage/ads?offset=1&limit=2
     *
     * @param ad
     * @param offset
     * @param limit
     * @return
     */
    @RequestMapping
    public ModelAndView ads(Ad ad, Integer offset, Integer limit) {
        ModelAndView mv = new ModelAndView("ad");
        List<Ad> ads = adService.findByAd(ad, offset, limit);
        mv.addObject("ads", ads);
        return mv;
    }

    /**
     * 新增或修改字典信息页面，使用 get 跳转到页面
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    public ModelAndView add(Long id) {
        ModelAndView mv = new ModelAndView("ad_add");
        Ad ad;
        if(id == null){
            //如果 id 不存在，就是新增数据，创建一个空对象即可
            ad = new Ad();
            mv.addObject("admsg", "添加广告");
        } else {
            //如果 id 存在，就是修改数据，把原有的数据查询出来
            ad = adService.findById(id);
            mv.addObject("admsg", "修改广告");
        }
        mv.addObject("model", ad);
        return mv;
    }

    /**
     * 新增或修改字典信息，通过表单 post 提交数据
     *
     * @param ad
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView save(Ad ad) {
        ad.setUpdateTime(sdf.format(new Date(System.currentTimeMillis())));
        ModelAndView mv = new ModelAndView();
        int operation;
        if(ad.getId() == null){
            operation = 1;
        } else {
            operation = 2;
        }
        try {
            if(adService.saveOrUpdate(ad)){
                AdMessage adMessage = new AdMessage();
                adMessage.setId(ad.getId());
                adMessage.setOperation(operation);
                adMessage.setUuidKey(UUID.randomUUID().toString());
                adMessage.setContent(JSONObject.toJSONString(ad));
                adService.sendMq(JSONObject.toJSONString(adMessage));
            }
            mv.setViewName("redirect:/ads");
        } catch (Exception e){
            mv.setViewName("ad_add");
            mv.addObject("msg", e.getMessage());
            mv.addObject("model", ad);
        }
        return mv;
    }

    /**
     * 通过 id 删除字典信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public ModelMap delete(@RequestParam Long id) {
        ModelMap modelMap = new ModelMap();
        try {
            boolean success = adService.deleteById(id);
            modelMap.put("success", success);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("msg", e.getMessage());
        }
        return modelMap;
    }

}
