package com.haoli.yuki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.yuki.dao.WechatAppletDao;
import com.haoli.yuki.domain.WechatAppletInfo;

@Service
public class WechatAppletService {
	
	@Autowired
	private WechatAppletDao wechatAppletDao;

	public WechatAppletInfo getWechatAppletInfoBySysCode(String sysCode) {
		return wechatAppletDao.getWechatAppletInfoBySysCode(sysCode);
	}

}
