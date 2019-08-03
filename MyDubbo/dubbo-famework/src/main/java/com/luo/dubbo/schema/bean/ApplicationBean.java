package com.luo.dubbo.schema.bean;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.luo.dubbo.context2.ContextFactory;
import com.luo.dubbo.schema.bean.annotation.Inject;
import com.luo.dubbo.util.ThreadUtils;

public abstract class ApplicationBean implements DubboBean, ApplicationContextAware {

    /****
     * spring中的 id
     */
    @Inject
    private String id;

    /** ---------------------getter setter------------------- */
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    static ApplicationContext SPRING;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // TODO Auto-generated method stub
        SPRING = applicationContext;
        ContextFactory.spring().setSpringContext(applicationContext);
    }

    /***
     * spring 设置bean的 id 回调 name : id 值
     */
    public void setBeanName(String name) {
        // 将spring注入的 dubbo配置 注入到 dubbo容器中
        System.err.println("$$$$$$$$$$$$$$$$$$$ " + name);
        if (SPRING != null && !ContextFactory.dubbo().isOnCreate()
                && ContextFactory.isWeb()) {
            ContextFactory.dubbo().onCreate();
        }
    }

    /***
     * spring 销毁时调用
     */
    public void destroy() throws Exception {
        // TODO Auto-generated method stub
        // System.out.println("destroy   :  ");
    }

}
