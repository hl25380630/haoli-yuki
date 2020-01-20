package com.haoli.yuki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.yuki.dao.UserProfileDao;
import com.haoli.yuki.domain.UserProfile;

@Service
public class UserProfileService {
	
	@Autowired
	private UserProfileDao userProfileDao;

	public UserProfile getUserProfile(Long userId) {
		return userProfileDao.getUserProfile(userId);
	}

	public Integer add(UserProfile profile) {
		return userProfileDao.add(profile);
	}
	
	
}
