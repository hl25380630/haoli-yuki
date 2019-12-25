package com.haoli.yuki.dao;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.yuki.domain.User;

@Mapper
public interface UserDao {

	Integer add(User user);

	User getByUserName(String userName);

}
