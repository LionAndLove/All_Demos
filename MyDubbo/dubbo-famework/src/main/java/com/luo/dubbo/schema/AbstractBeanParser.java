package com.luo.dubbo.schema;

import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import com.luo.dubbo.util.Assert;

public abstract class AbstractBeanParser implements BeanDefinitionParser {

    /****
     * spring中的 bean 的 class
     */
    private Class<?> beanClass;

    public AbstractBeanParser(Class<?> beanClass) {
        Assert.isNull(beanClass, "dubbo bean class 不能为空");
        this.beanClass = beanClass;
    }

    /***
     * 获取spring 的 RootBeanDefinition
     * 
     * @return
     * @author HadLuo 2017年11月15日 新建
     */
    public RootBeanDefinition springBeanDefinition() {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        return beanDefinition;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }
}
