package com.jikeh.demo;

import com.jikeh.hystrix.CommandHelloWorld;
import org.junit.Test;

import java.util.concurrent.Future;

/**
 * Hystrix基础实例1：单个请求
 */
public class UnitTestCommand {

    //同步：
    @Test
    public void testSynchronous() {

        String str = new CommandHelloWorld("World").execute();
        System.out.println(str);

    }

    //异步：
    @Test
    public void testAsynchronous() throws Exception {

        Future<String> fWorld = new CommandHelloWorld("World").queue();
        String str = fWorld.get();
        System.out.println(str);

    }

}
