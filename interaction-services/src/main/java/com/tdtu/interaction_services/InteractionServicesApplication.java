package com.tdtu.interaction_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InteractionServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(InteractionServicesApplication.class, args);
	}

}
