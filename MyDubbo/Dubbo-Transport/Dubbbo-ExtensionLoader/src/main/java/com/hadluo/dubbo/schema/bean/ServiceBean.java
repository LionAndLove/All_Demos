package com.hadluo.dubbo.schema.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.hadluo.dubbo.schema.ServiceConfig;
import com.hadluo.dubbo.schema.SpringBeanLifecycle;

public class ServiceBean extends ServiceConfig implements SpringBeanLifecycle {
    private transient boolean exported;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (ContextRefreshedEvent.class.getName().equals(event.getClass().getName())) {

            if (exported) {
                return;
            }
            setExported(true);
            export();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.err.println("spring init " + ServiceBean.class.getName() + " bean");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setApplicationContext(applicationContext);
    }

    @Override
    public void setBeanName(String name) {
    }

    public synchronized void setExported(boolean exported) {
        this.exported = exported;
    }

    public synchronized boolean exported() {
        return exported;
    }

    @Override
    public void destroy() throws Exception {
        setApplicationContext(null);
        setExported(false);
    }
}
