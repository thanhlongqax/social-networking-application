package com.tdtu.search_services.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.checkerframework.checker.units.qual.N;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @Bean
    public NewTopic friendRequestTopic(){
        return new NewTopic("friend-request", 2, (short) 1);
    }
}
