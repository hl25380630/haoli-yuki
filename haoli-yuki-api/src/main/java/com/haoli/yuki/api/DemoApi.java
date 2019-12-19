package com.haoli.yuki.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.yuki.service.DemoService;

@RestController
public class DemoApi {
	
	@Autowired
	private DemoService demoService;

}
