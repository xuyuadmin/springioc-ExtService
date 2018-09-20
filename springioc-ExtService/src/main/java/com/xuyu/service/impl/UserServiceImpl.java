package com.xuyu.service.impl;

import com.xuyu.service.UserService;
import com.xuyu.spring.annotation.ExtService;

//user 服务层
@ExtService
public class UserServiceImpl implements UserService {

	public void add() {
		System.out.println("使用Java反射机制初始化对象");
	}
}
