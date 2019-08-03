package com.luo.dubbo.schema;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {

    public static ApplicationContext SPRING = null;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SPRING = applicationContext;
        // TODO Auto-generated method stub
        System.out.println("%%%%%%%%%%%%%%%% " + applicationContext);
    }
}
