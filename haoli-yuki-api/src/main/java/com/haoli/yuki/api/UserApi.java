package com.haoli.yuki.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.sdk.web.domain.JsonResponse;
import com.haoli.yuki.domain.User;
import com.haoli.yuki.service.UserService;

@RestController
public class UserApi {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/user/register")
	public JsonResponse<String> userRegister(@RequestBody User user) throws Exception{
		userService.userRegister(user);
		return JsonResponse.success();
	}
	
	@PostMapping("/user/login")
	public JsonResponse<String> userLogin(@RequestBody Map<String, Object> params){
		userService.userLogin(params);
		return JsonResponse.success();
	}

}
