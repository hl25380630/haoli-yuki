package com.haoli.yuki.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.yuki.service.DemoService;

@RestController
public class DemoController {
	
	@Autowired
	private DemoService demoService;

}
