package com.haoli.yuki.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.sdk.web.domain.RsaProperties;
import com.haoli.sdk.web.exception.ConditionException;
import com.haoli.sdk.web.util.Md5Util;
import com.haoli.sdk.web.util.RsaUtil;
import com.haoli.yuki.dao.UserDao;
import com.haoli.yuki.domain.User;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RsaProperties rsaProperties;

	public void userRegister(User user) throws Exception{
		String userName = user.getUserName();
		User dbUser = this.getByUserName(userName);
		if(dbUser != null) {
			throw new ConditionException("User already exists!");
		}
		String encryptPassword = user.getPassword();
		String privateKey = rsaProperties.getPrivateKey();
		String rawPassword = RsaUtil.decrypt(encryptPassword, privateKey);
		String salt = System.currentTimeMillis() + userName;
		String password = Md5Util.sign(rawPassword, salt, "UTF-8");
		user.setPassword(password);
		user.setCreateTime(new Date());
		userDao.add(user);
	}
	
	public User getByUserName(String userName) {
		return userDao.getByUserName(userName);
	}
	
	public void userLogin(Map<String, Object> params) {
	}

}
