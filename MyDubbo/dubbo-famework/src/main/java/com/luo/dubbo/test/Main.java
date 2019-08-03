package com.luo.dubbo.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.luo.dubbo.schema.bean.ServiceBean;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");  
        ServiceBean p = (ServiceBean)ctx.getBean("a");
        System.out.println(p.getRef());  
    }
}
