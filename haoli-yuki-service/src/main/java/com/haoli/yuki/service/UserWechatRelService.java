package com.haoli.yuki.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.haoli.yuki.dao.UserWechatRelDao;
import com.haoli.yuki.domain.UserWechatRel;

public class UserWechatRelService {
	
	@Autowired
	private UserWechatRelDao userWechatRelDao;

	public UserWechatRel getBySourceAndOpenId(Map<String, Object> params) {
		return userWechatRelDao.getBySourceAndOpenId(params);
	}

	public Integer add(UserWechatRel userWechatRel) {
		return userWechatRelDao.add(userWechatRel);
	}
	

}
