package com.haoli.yuki.dao;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.yuki.domain.WechatAppletInfo;

@Mapper
public interface WechatAppletDao {

	WechatAppletInfo getWechatAppletInfoBySysCode(String sysCode);

}
