package com.toby.spi.impl;

import com.toby.spi.ISayName;

public class SayChineseName implements ISayName {
    @Override
    public void say() {
        System.out.println("肥朝");
    }
}
