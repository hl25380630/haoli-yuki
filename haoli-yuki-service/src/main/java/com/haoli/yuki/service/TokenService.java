package com.haoli.yuki.service;

import org.springframework.stereotype.Service;

import com.haoli.sdk.web.domain.UserClient;
import com.haoli.sdk.web.util.AesSupport;

import eu.bitwalker.useragentutils.UserAgent;

@Service
public class TokenService {
	
	public String buildUt(String userId, String agent, String ip) throws Exception {
		UserClient userClient = this.getUserClient(agent);
		StringBuilder source = new StringBuilder(userId);
		source.append("^").append(userClient.getClientId());
		source.append("^").append(System.currentTimeMillis());
		source.append("^").append(ip);
		AesSupport aesSupport = new AesSupport();
		return aesSupport.encrypt(source.toString());
	}
	
	public UserClient getUserClient(String agent) {
		UserAgent userAgent = UserAgent.parseUserAgentString(agent);
		String clientId = String.valueOf(userAgent.getId());
		String clientType = null;
		if(agent.contains("MicroMessenger")) {
			clientType = "wechat";
		} else if(agent.contains("Android")){
			clientType = "android";
		} else if(agent.contains("iPhone")){
			clientType = "ios";
		} else {
			clientType = "other";
		}
		return new UserClient(clientType, clientId);
	}

}
