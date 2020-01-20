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

	public void userAutoRegister(User user) throws Exception{
		String userName = user.getUserName();
		User dbUser = this.getByUserName(userName);
		if(dbUser != null) {
			throw new ConditionException("该号码已经被别人使用啦,换一个吧~");
		}
		String rawPassword = autoRegisterInitialPwd;
		String salt = System.currentTimeMillis() + userName;
		String password = Md5Util.sign(rawPassword, salt, "UTF-8");
		user.setPassword(password);
		user.setSalt(salt);
		user.setCreateTime(new Date());
		userDao.add(user);
	}
	
	public Map<String, Object> userLogin(HttpServletRequest request, Map<String, Object> params) throws Exception{
		String userName = MapUtil.getString(params, "userName");
		String encryptPassword = MapUtil.getString(params, "password");
		String privateKey = rsaProperties.getPrivateKey();
		String rawPassword = RsaUtil.decrypt(encryptPassword, privateKey);
		User dbUser = this.getByUserName(userName);
		if(dbUser == null) {
			throw new ConditionException("该会员号不存在，请重试~");
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
