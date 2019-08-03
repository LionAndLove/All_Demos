package com.luo.dubbo.schema.bean;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;

/***
 * dubbo标签的 bean
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年11月15日 新建
 */
public interface DubboBean extends BeanNameAware, DisposableBean {

    public Class<? extends DubboBean> getBeanClass();

    public String getName();
}
