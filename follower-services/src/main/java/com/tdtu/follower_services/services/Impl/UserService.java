package com.tdtu.follower_services.services.Impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.follower_services.dto.ResDTO;
import com.tdtu.follower_services.models.User;
import com.tdtu.follower_services.services.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
public class UserService implements IUserService {
    @Value("${service.user-service.host}")
    private String userServiceHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public User findById(String userId){
        String getUserInfoUrl = UriComponentsBuilder
                .fromHttpUrl(userServiceHost + "api/users/" + userId)
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

    public List<User> findUserFriendIdsByUserToken(String token) {
        String getUserFriendsUrl = UriComponentsBuilder
                .fromHttpUrl(userServiceHost + "api/users/friends")
                .toUriString();

        log.info(getUserFriendsUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ResDTO> responseEntity = restTemplate.exchange(
                    getUserFriendsUrl,
                    HttpMethod.GET,
                    entity,
                    ResDTO.class);

            ResDTO<?> response = responseEntity.getBody();
            List<User> userObjects = new ArrayList<>();
            if (response != null && response.getData() != null) {
                userObjects = objectMapper.convertValue(response.getData(), new TypeReference<List<User>>() {});
                log.info("[Fetched] response data: " + response.getData());
            }

            return userObjects;
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}