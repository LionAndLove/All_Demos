package com.toby.spi.impl;

import com.toby.spi.ISayName;

public class SayEnglishName implements ISayName {
    @Override
    public void say() {
        System.out.println("Toby");
    }
}
