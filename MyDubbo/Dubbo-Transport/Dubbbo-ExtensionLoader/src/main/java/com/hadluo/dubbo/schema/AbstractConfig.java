package com.hadluo.dubbo.schema;

import org.springframework.context.ApplicationContext;

public abstract class AbstractConfig {

    private ApplicationContext applicationContext;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

}
