package com.tdtu.newsfeed_service.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.newsfeed_service.dtos.ResDTO;
import com.tdtu.newsfeed_service.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.http.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FollowerService {
    @Value("${service.follower-service.host}")
    private String followerServiceHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<String> findFollowedUserIdsByToken(String token) {
        String getFollowedInfoUrl = UriComponentsBuilder
                .fromHttpUrl(followerServiceHost + "/api/followers/get_followed_ids")
                .toUriString();

        log.info("Fetching followed user IDs from: {}", getFollowedInfoUrl);

        // Tạo headers và thêm Authorization token
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ResDTO> responseEntity = restTemplate.exchange(
                    getFollowedInfoUrl,
                    HttpMethod.GET,
                    entity,
                    ResDTO.class
            );


            ResDTO<?> response = responseEntity.getBody();

            if (response != null && response.getData() != null) {
                List<String> followedIdList = objectMapper.convertValue(response.getData(), new TypeReference<List<String>>() {});
                log.info("Followed Fetched followed user IDs: {}", followedIdList);
                return followedIdList;
            }
        } catch (HttpClientErrorException e) {
            log.error("Error fetching followed user IDs: {}", e.getMessage());
        }

        return Collections.emptyList();
    }
}