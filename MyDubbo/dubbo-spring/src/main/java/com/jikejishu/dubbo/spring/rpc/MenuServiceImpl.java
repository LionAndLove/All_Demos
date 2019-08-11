package com.jikejishu.dubbo.spring.rpc;

public class MenuServiceImpl implements MenuService{
    @Override
    public void sayHello() {
        System.out.println("执行具体menu方法");
    }
}
