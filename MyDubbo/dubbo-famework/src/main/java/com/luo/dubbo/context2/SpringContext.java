package com.luo.dubbo.context2;

import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import com.luo.dubbo.util.Assert;

/***
 * 抽象 spring context（spring上下文, 面向用户； beanfactory是面向底层的） <=> ApplicationContext
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月15日 新建
 */
public abstract class SpringContext extends AbstractContext {

    /***
     * spring context
     */
    private BeanFactory springContext;

    //获取spring容器
    BeanFactory spring() {
        return springContext;
    }

    /**
     * 加载配置文件初始化springContext
     * @param springContext
     */
    public void setSpringContext(BeanFactory springContext) {
        this.springContext = springContext;
    }

    public void onDestory() {
        // TODO Auto-generated method stub
    }

    //初始化spring容器FileSystemXmlAppSpringContext#onCreate
    public void setBeanFactory(BeanFactory springContext) {
        Assert.isNull(springContext, "Bean Factory is null");
        setSpringContext(springContext);
    }

    /***
     * 动态 设置 bean(解析dubbo标签的时候, 将bean注册到spring容器中,即：通过beanfactory可以获取bean)
     * BeanParser#parse
     * 
     * @param name
     * @param beanClass
     * @param properties
     * @author HadLuo 2017年11月17日 新建
     */
    public void setBean(String name, Class<?> beanClass, Map<String, Object> properties) {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) spring();
        // Bean的实例工厂
        DefaultListableBeanFactory dbf = (DefaultListableBeanFactory) context.getBeanFactory();
        // Bean构建 BeanService.class 要创建的Bean的Class对象
        BeanDefinitionBuilder dataSourceBuider = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        // 向里面的属性注入值，提供get set方法
        if (properties != null && properties.size() > 0) {
            for (String key : properties.keySet()) {
                dataSourceBuider.addPropertyValue(key, properties.get(key));
            }
        }
        // 将实例注册spring容器中 bs 等同于 id配置
        dbf.registerBeanDefinition(name, dataSourceBuider.getBeanDefinition());

    }

    /**
     * 从spring容器中获取bean
     * @param name
     * @return
     */
    public Object getBean(String name) {
        return spring().getBean(name);
    }

    public <T> T getBean(Class<T> type) {
        Map<String, T> beans = getBeansOfType(type);
        for (String id : beans.keySet()) {
            return beans.get(id);
        }
        return null;
    }
}
