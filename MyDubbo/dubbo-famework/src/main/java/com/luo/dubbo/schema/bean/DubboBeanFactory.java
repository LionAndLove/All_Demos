package com.luo.dubbo.schema.bean;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class DubboBeanFactory {

    /***
     * 创建 dubbo bean对象
     * 
     * @param clazz
     * @param properties 属性值
     * @return
     * @author HadLuo 2017年12月15日 新建
     */
    public static DubboBean createBean(Class<?> clazz, Map<String, Object> properties) {
        try {
            DubboBean dubboBean = (DubboBean) clazz.newInstance();
            BeanUtils.populate(dubboBean, properties);
            return dubboBean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建bean失败, bean clazz:" + clazz.getName());
        }
    }

}
