package com.tdtu.search_services.service.Impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.model.User;
import com.tdtu.search_services.service.UserService;
import com.tdtu.search_services.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${service.user-service.host}")
    private String userServiceHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    public List<User> findByIds(List<String> ids){
        String getUserInfoUrl = UriComponentsBuilder
                .fromHttpUrl(userServiceHost + "api/users/by-ids")
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

