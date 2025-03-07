package com.tdtu.friend_recommendation_services.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.friend_recommendation_services.dto.ResDTO;
import com.tdtu.friend_recommendation_services.dto.request.UserToElasticSearchDTO;

import com.tdtu.friend_recommendation_services.model.User;
import com.tdtu.friend_recommendation_services.service.IUserSearchService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSearchService implements IUserSearchService {
    @Value("${service.search-service.host}")

    private String searchServiceHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    public ResDTO<?> saveUserToElasticSearch(User user){
        String getUserSearchInfoUrl = UriComponentsBuilder
                .fromHttpUrl(searchServiceHost + "/api/search/save")
                .toUriString();

        log.info(getUserSearchInfoUrl);

        UserToElasticSearchDTO userToElasticSearchDTO = new UserToElasticSearchDTO();
        userToElasticSearchDTO.setId(user.getId());
        userToElasticSearchDTO.setFirstName(user.getFirstName());
        userToElasticSearchDTO.setLastName(user.getLastName());
        userToElasticSearchDTO.setMiddleName(user.getMiddleName());
        userToElasticSearchDTO.setUserFullName(user.getUserFullName());

        try {
            ResDTO<?> response = restTemplate.postForObject(getUserSearchInfoUrl, userToElasticSearchDTO, ResDTO.class);
            log.info("day la respone :{}",response);
            if (response != null ) {
                log.info("[Fetched] response data: " + response.getData());
                return new ResDTO<>(HttpServletResponse.SC_OK, "No content", null);
            }
            return new ResDTO<>(204, "No content", null);

        }catch (HttpClientErrorException e){
            log.error(e.getMessage());
            return null;
        }
    }
}

