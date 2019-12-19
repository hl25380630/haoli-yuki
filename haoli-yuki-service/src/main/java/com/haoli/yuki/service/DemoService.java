package com.haoli.yuki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.yuki.dao.DemoDao;

@Service
public class DemoService {
	
	@Autowired
	private DemoDao demoDao;
	
	

}
