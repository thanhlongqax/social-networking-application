package com.tdtu.interaction_services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(
                                "/api/*",
                                "/api-docs/**", // Allow access to OpenAPI docs
                                "/swagger-ui/**",     // Allow access to Swagger UI
                                "/swagger-ui.html"

                        ).permitAll()
                        .anyRequest().permitAll()
                )
        ;

        return http.build();
    }

}
