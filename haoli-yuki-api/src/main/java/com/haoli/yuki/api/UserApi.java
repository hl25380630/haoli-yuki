package com.haoli.yuki.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/user/applet/login")
	public JsonResponse<Map<String, Object>> userAppletLogin(@RequestParam String code) throws Exception{
		Map<String, Object> result = userService.userAppletLogin(code);
		return new JsonResponse<Map<String, Object>>(result);
	}

}
