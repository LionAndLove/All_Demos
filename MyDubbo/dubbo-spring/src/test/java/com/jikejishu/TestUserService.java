package com.jikejishu;

import com.jikejishu.dubbo.spring.rpc.HelloService;
import com.jikejishu.dubbo.spring.rpc.MenuService;
import com.jikejishu.dubbo.spring.rpc.ProxyFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 这是测试spring与mybatis的整合：
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application.xml" })
public class TestUserService {

	@Autowired
	private MenuService menuService;

	@Autowired
	private HelloService helloService;

	@Test
	public void test() {
		ProxyFactory proxyFactory = new ProxyFactory(MenuService.class);
		MenuService menuService = proxyFactory.getProxyObject();
		menuService.sayHello();
	}

	@Test
	public void test2(){
		menuService.sayHello();
		helloService.sayHello();
	}

}
