package com.jikeh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	/**
	 * 调用模板引擎进行解析：http://localhost:1111/ad-manage/
	 * @param map
	 * @return
	 */
	@RequestMapping(value = {"", "/index"})
	public String welcome(ModelMap map) {
		map.addAttribute("site", "极客慧");
		return "index";
	}

}
