package com.luo.dubbo.schema.bean.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 
 * 代表要注入dubbo容器的属性字段
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月15日 新建
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    // TODO
    public String alias() default "";
}
