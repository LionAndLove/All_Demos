package com.luo.dubbo.context2;

import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;

/***
 * Web 启动方式的 spring
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月15日 新建
 */
public class WebAppSpringContext extends SpringContext {

    private Logger log = Logger.getLogger(WebAppSpringContext.class);

    public WebAppSpringContext() {
    }

    public void onCreate() {
        log.info("开始加载WebSpringContext...");
    }

    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        // TODO Auto-generated method stub
        return ((WebApplicationContext) spring()).getBeansOfType(type);
    }

}
