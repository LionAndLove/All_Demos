package com.hadluo.dubbo.schema;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/***
 * Spring bean 的生命周期 <br>
 * InitializingBean: bean 初始化调用，优先init-method配置 <br>
 * DisposableBean : bean 销毁调用 ApplicationContextAware : 取得 spring context
 * BeanNameAware : 取得bean的名称
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年4月28日 新建
 */
public interface SpringBeanLifecycle extends InitializingBean, DisposableBean,
        ApplicationContextAware, ApplicationListener<ApplicationEvent>, BeanNameAware {
}
