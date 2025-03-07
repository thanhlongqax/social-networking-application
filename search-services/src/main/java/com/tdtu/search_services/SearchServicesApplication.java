package com.tdtu.search_services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SearchServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchServicesApplication.class, args);
	}

}
