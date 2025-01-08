package com.tdtu.newsfeed_service.services;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.newsfeed_service.dtos.ResDTO;
import com.tdtu.newsfeed_service.enums.EReactionType;
import com.tdtu.newsfeed_service.models.Comment;
import com.tdtu.newsfeed_service.models.Reacts;
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
@RequiredArgsConstructor
@Slf4j
public class InteractionService {
    @Value("${service.interaction-service.host}")
    private String interactionService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<Comment> findCommentsByPostId(String token , String postId){
        String getCommentsUrl = UriComponentsBuilder
                .fromHttpUrl(interactionService + "/api/v1/comments")
                .queryParam("postId", postId)
                .toUriString();

        log.info(getCommentsUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ResDTO> response = restTemplate.exchange(getCommentsUrl, HttpMethod.GET, entity, ResDTO.class);
            List<Comment> comments = new ArrayList<>();
            if(response.getBody() != null && response.getBody().getData() != null){
                comments = objectMapper.convertValue(response.getBody().getData(), new TypeReference<List<Comment>>(){});
                log.info("[Fetched] response data: " + response.getBody().getData());
            }

            return comments;
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Map<EReactionType, List<Reacts>> findReactionsByPostId(String token, String postId){
        String getReactsUrl = UriComponentsBuilder
                .fromHttpUrl(interactionService + "/api/v1/reacts")
                .queryParam("postId", postId)
                .toUriString();

        log.info(getReactsUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ResDTO> response = restTemplate.exchange(getReactsUrl, HttpMethod.GET, entity, ResDTO.class);
            Map<EReactionType, List<Reacts>> reacts = new HashMap<>();
            if(response.getBody() != null && response.getBody().getData() != null){
                reacts = objectMapper.convertValue(response.getBody().getData(), new TypeReference<Map<EReactionType, List<Reacts>>>(){});
                log.info("[Fetched] response data: " + response.getBody().getData());
            }

            return reacts;
        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}