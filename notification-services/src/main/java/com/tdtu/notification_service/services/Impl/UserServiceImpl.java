package com.tdtu.notification_service.services.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.notification_service.dtos.ResDTO;
import com.tdtu.notification_service.models.User;
import com.tdtu.notification_service.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${service.user-service.host}")
    private String userServiceHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public User findById(String userId){
        String getUserInfoUrl = UriComponentsBuilder
                .fromHttpUrl(userServiceHost + "api/v1/users/" + userId)
                .toUriString();

        log.info(getUserInfoUrl);

        try {
            ResDTO<?> response = restTemplate.getForObject(getUserInfoUrl, ResDTO.class);
            User userObject = new User();
            if(response != null && response.getData() != null){
                userObject = objectMapper.convertValue(response.getData(), new TypeReference<User>(){});
                log.info("[Fetched] response data: " + response.getData());
            }

            return userObject;
        }catch (HttpClientErrorException e){
            log.error(e.getMessage());
            return null;
        }
    }

    public List<User> findByIds(List<String> ids){
        String getUserInfoUrl = UriComponentsBuilder
                .fromHttpUrl(userServiceHost + "api/v1/users/by-ids")
                .toUriString();

        log.info(getUserInfoUrl);

        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("userIds", ids);

        try {
            ResDTO<?> response = restTemplate.postForObject(getUserInfoUrl, requestBody, ResDTO.class);
            List<User> userObjects = new ArrayList<>();
            if(response != null && response.getData() != null){
                userObjects = objectMapper.convertValue(response.getData(), new TypeReference<List<User>>(){});
                log.info("[Fetched] response data: " + response.getData());
            }

            return userObjects;
        }catch (HttpClientErrorException e){
            log.error(e.getMessage());
            return null;
        }
    }
}