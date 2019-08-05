package com.hadluo.dubbo.http;

import com.hadluo.dubbo.extension.spi.Adaptive;
import com.hadluo.dubbo.extension.spi.SPI;

@SPI("get")
public interface Http {
    @Adaptive
    public void http();
}
