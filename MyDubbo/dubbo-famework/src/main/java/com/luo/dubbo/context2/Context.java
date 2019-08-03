package com.luo.dubbo.context2;

import java.util.Map;

public interface Context extends LifeCycle {

    /**
     * 动态设置 bean
     * 
     * @param name
     * @param beanClass
     * @param properties
     * @author HadLuo 2017年12月15日 新建
     */
    public void setBean(String name, Class<?> beanClass, Map<String, Object> properties);

    /***
     * 通过bean id 获取 bean
     * 
     * @param name
     * @return
     * @author HadLuo 2017年12月15日 新建
     */
    public Object getBean(String name);

    /***
     * 根据类型 获取 bean集合
     * 
     * @param type
     * @return
     * @author HadLuo 2017年12月15日 新建
     */
    public <T> Map<String, T> getBeansOfType(Class<T> type);

    /***
     * 根据类型获取bean
     * @param type
     * @return
     * @author HadLuo  2017年12月15日 新建
     * @param <T>
     */
    public <T> T getBean(Class<T> type);

}
