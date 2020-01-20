package com.haoli.yuki.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.yuki.domain.UserWechatRel;

@Mapper
public interface UserWechatRelDao {

	UserWechatRel getBySourceAndOpenId(Map<String, Object> params);

	Integer add(UserWechatRel userWechatRel);

}
