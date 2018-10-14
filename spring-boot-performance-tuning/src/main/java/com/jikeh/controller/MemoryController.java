package com.jikeh.controller;

import com.jikeh.model.User;
import com.jikeh.service.Metaspace;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MemoryController {

	private List<User> userList = new ArrayList<>();
	private List<Object>  classList = new ArrayList<>();

	/**
	 * 堆OOM
	 * -Xmx64M -Xms64M
	 * */
	@RequestMapping("/heap")
	public String heap() {
		int i=0;
		while(true) {
			User user = new User(i++, UUID.randomUUID().toString());
			//将user对象添加到list列表中，因为存在引用关系所以user对象不会被GC回收
			userList.add(user);
		}
	}

	/**
	 * 元空间OOM
	 * -XX:MetaspaceSize=64M -XX:MaxMetaspaceSize=64M
	 * */
	@RequestMapping("/nonheap")
	public String nonheap() {
		while(true) {
			classList.addAll(Metaspace.createClasses());
		}
	}
	
}
