package com.haoli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableConfigurationProperties
@EnableAsync
public class YukiConsoleApp {
	
	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(YukiConsoleApp.class, args);
	}

}
