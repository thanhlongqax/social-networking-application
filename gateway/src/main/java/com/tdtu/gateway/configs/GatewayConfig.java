package com.tdtu.gateway.configs;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GatewayConfig {
    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/users/**" )
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .route("search-service", r -> r.path("/api/search/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://search-service"))
                .route("message-service", r -> r.path("/api/rooms/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://message-service"))
                .route("newsfeed-service", r -> r.path("/api/posts/**", "/api/report/**", "/api/banned-word/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://newsfeed-service"))
                .route("follower-service", r -> r.path("/api/followers/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://follower-service"))
                .route("interaction-service", r -> r.path("/api/comments/**", "/api/reacts/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://interaction-service"))
                .route("file-service", r -> r.path("/api/file/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://file-service"))
                .build();
    }
}