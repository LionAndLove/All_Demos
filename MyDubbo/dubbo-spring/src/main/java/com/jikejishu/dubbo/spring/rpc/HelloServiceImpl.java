package com.jikejishu.dubbo.spring.rpc;

public class HelloServiceImpl implements HelloService{
    @Override
    public void sayHello() {
        System.out.println("执行具体hello方法");
    }
}
