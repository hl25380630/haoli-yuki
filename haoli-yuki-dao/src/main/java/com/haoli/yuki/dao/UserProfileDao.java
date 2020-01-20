package com.haoli.yuki.dao;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.yuki.domain.UserProfile;

@Mapper
public interface UserProfileDao {

	UserProfile getUserProfile(Long userId);

	Integer add(UserProfile profile);

}
