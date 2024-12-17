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
                .route("user-services", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-services"))
                .route("auth-services", r -> r.path("/api/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-services"))
                .route("search-services", r -> r.path("/api/search/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://search-services"))
                .route("message-services", r -> r.path("/api/rooms/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://message-services"))
                .route("newsfeed-services", r -> r.path("/api/newsfeeds/**", "/api/report/**", "/api/banned-word/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://newsfeed-services"))
                .route("interaction-services", r -> r.path("/api/comments/**", "/api/reacts/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://interaction-services"))
                .route("file-services", r -> r.path("/api/file/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://file-services"))
                .build();
    }
}