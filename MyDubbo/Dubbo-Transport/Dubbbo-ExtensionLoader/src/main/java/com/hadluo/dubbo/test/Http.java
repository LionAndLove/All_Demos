package com.hadluo.dubbo.test;

import com.hadluo.dubbo.extension.Adaptive;
import com.hadluo.dubbo.extension.SPI;

@SPI("get")
public interface Http {
    @Adaptive
    public void http();
}
