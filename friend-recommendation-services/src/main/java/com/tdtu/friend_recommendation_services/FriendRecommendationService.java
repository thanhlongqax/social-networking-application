package com.tdtu.friend_recommendation_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FriendRecommendationService {

	public static void main(String[] args) {
		SpringApplication.run(FriendRecommendationService.class, args);
	}

}
