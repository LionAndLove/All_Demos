package com.jikeh.hystrix;

import static org.junit.Assert.*;

import java.util.concurrent.Future;

import org.junit.Test;

import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * Hystrix基础实例1：单个请求
 *
 * @author 极客慧 www.jikeh.cn
 */
public class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("GetAdsGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        return "Hello " + name + "!";
    }

}
