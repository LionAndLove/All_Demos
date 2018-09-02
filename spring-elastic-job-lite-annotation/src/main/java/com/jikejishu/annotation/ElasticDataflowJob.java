package com.jikejishu.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 更多免费资料，更多高清内容，更多java技术，欢迎访问网站
 * 极客慧：www.jikeh.cn
 * 如果你希望进一步深入交流，请加入我们的大家庭QQ群：375412858
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticDataflowJob {

    @AliasFor("cron")
    public abstract String value() default "";

    @AliasFor("value")
    public abstract String cron() default "";

    public abstract int shardingTotalCount() default 1;

    public abstract String shardingItemParameters() default "";

    public abstract String dataSource() default "";

    public abstract boolean disabled() default false;

    public abstract boolean streamingProcess() default true;
}
