package com.tdtu.follower_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FollowerServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FollowerServicesApplication.class, args);
	}

}
