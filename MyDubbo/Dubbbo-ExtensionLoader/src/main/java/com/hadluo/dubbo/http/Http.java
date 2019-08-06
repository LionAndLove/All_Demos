package com.hadluo.dubbo.http;

import com.hadluo.dubbo.extension.spi.Adaptive;
import com.hadluo.dubbo.extension.spi.SPI;

@SPI("get")
public interface Http {
    /**
     * 本来是要动态编译来实现动态代理, 在这里使用JDK来实现动态代理
     * 区别：
     *  动态编译：字节码技术
     *  JDK：反射技术
     */
    @Adaptive
    public void http();
}
