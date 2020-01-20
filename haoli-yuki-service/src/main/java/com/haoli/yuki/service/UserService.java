package com.haoli.yuki.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.haoli.sdk.web.exception.ConditionException;
import com.haoli.sdk.web.util.HttpUtil;
import com.haoli.sdk.web.util.HttpUtil.HttpResponse;
import com.haoli.sdk.web.util.IpUtil;
import com.haoli.sdk.web.util.MapUtil;
import com.haoli.sdk.web.util.Md5Util;
import com.haoli.sdk.web.util.RsaUtil;
import com.haoli.yuki.dao.UserDao;
import com.haoli.yuki.domain.RsaProperties;
import com.haoli.yuki.domain.User;
import com.haoli.yuki.domain.UserProfile;
import com.haoli.yuki.domain.UserWechatRel;
import com.haoli.yuki.domain.WechatAppletInfo;
import com.haoli.yuki.service.constant.UserWechatRelConstant;
import com.haoli.yuki.service.constant.WechatAppletConstant;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RsaProperties rsaProperties;
	
	@Autowired
	private UserWechatRelService userWechatRelService;
	
	@Autowired
	private UserProfileService userProfileService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private WechatAppletService wechatAppletService;
	
	@Value("${wechat.applet.login.url}")
	private String wechatAppletLoginUrl;
	
	@Value("${auto.register.initial.password}")
	private String autoRegisterInitialPwd;
	
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
	
	
	public boolean checkUserNameIsRegistered(String userName) {
		boolean flag = true;
		User dbUser = this.getByUserName(userName);
		//判断该用户名（手机号）是否已经被注册
		if(dbUser == null) {
			flag = false;
		}
		return flag;
	}

	public Map<String, Object> userAutoRegisterAndLogin(HttpServletRequest request, User user) throws Exception{
		Map<String, Object> result = new HashMap<String, Object>();
		String userName = user.getUserName();
		//自动注册
		String rawPassword = autoRegisterInitialPwd;
		String salt = System.currentTimeMillis() + userName;
		String password = Md5Util.sign(rawPassword, salt, "UTF-8");
		//添加用户信息到数据库
		user.setPassword(password);
		user.setSalt(salt);
		user.setCreateTime(new Date());
		userDao.add(user);
		//添加用户档案信息
		UserProfile profile = user.getUserProfile();
		userProfileService.add(profile);
		//生成用户ut
		Long userId = user.getId();
		String agent = request.getHeader("User-Agent");
		String ip = IpUtil.getIP(request);
		String ut = tokenService.buildUt(String.valueOf(userId), agent, ip);
		result.put("userName", userName);
		result.put("ut", ut);
		return result;
	}
	
	public Map<String, Object> userLogin(HttpServletRequest request, Map<String, Object> params) throws Exception{
		String userName = MapUtil.getString(params, "userName");
		String encryptPassword = MapUtil.getString(params, "password");
		String privateKey = rsaProperties.getPrivateKey();
		String rawPassword = RsaUtil.decrypt(encryptPassword, privateKey);
		User dbUser = this.getByUserName(userName);
		if(dbUser == null) {
			throw new ConditionException("该号码尚未注册");
		}
		String dbPassword = dbUser.getPassword();
		String salt = dbUser.getSalt();
		String password = Md5Util.sign(rawPassword, salt, "UTF-8");
		if(!dbPassword.equals(password)) {
			throw new ConditionException("密码错误，请重试~");
		}
		String agent = request.getHeader("User-Agent");
		Long userId = dbUser.getId();
		String ip = IpUtil.getIP(request);
		String ut = tokenService.buildUt(String.valueOf(userId), agent, ip);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("userName", userName);
		result.put("ut", ut);
		return result;
	}

	//等申请了企业级应用后再用
	public Map<String, Object> userAppletLogin(String jsCode) throws Exception {
		WechatAppletInfo appletInfo = wechatAppletService.getWechatAppletInfoBySysCode(WechatAppletConstant.SYS_CODE_BANDDREAM);
		String appid = appletInfo.getAppid();
		String secret = appletInfo.getSecret();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", appid);
		params.put("secret", secret);
		params.put("js_code", jsCode);
		params.put("grant_type", "authorization_code");
		HttpResponse response = HttpUtil.get(wechatAppletLoginUrl, params);
		String body = response.getBody();
		JSONObject jobj = JSONObject.parseObject(body);
		String openid = jobj.getString("openid");
		String sessionKey = jobj.getString("session_key");
		String unionid = jobj.getString("unionid");
		Map<String, Object> qparams = new HashMap<String, Object>();
		qparams.put("openid", openid);
		qparams.put("source", UserWechatRelConstant.SOURCE_APPLET);
		UserWechatRel userWechatRel = userWechatRelService.getBySourceAndOpenId(qparams);
		//若该用户的openid尚未关联用户id，则先关联
		if(userWechatRel == null) {
			
		}
		return null;
	}
	
	public User getByUserName(String userName) {
		return userDao.getByUserName(userName);
	}
	

}
