package com.jikeh.demo;

import org.junit.Test;

/**
 * 将这个功能用线程池隔离技术来实现：
 */
public class base {

    @Test
    public void testBase() {
        System.out.println(getStr("World"));
    }

    private String getStr(String name){
        return "Hello " + name + "!";
    }

}
