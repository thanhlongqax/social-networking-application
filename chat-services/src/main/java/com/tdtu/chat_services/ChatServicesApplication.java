package com.tdtu.chat_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class ChatServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatServicesApplication.class, args);
	}

}
