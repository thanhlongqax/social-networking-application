package com.tdtu.auth_services.service.Impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.auth_services.dto.ResDTO;
import com.tdtu.auth_services.dto.request.SignupRequest;
import com.tdtu.auth_services.dto.respone.SignUpRespone;
import com.tdtu.auth_services.model.User;
import com.tdtu.auth_services.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${service.user-service.host}")
    private String userServiceHost;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public User getUserInfo(String email){
        String getUserInfoUrl = userServiceHost + "api/v1/users/" + email + "/for-auth";
        try {
            ResDTO<?> response = restTemplate.getForObject(getUserInfoUrl, ResDTO.class);
            User userObject = new User();
            if(response != null && response.getData() != null){
                userObject = objectMapper.convertValue(response.getData(), User.class);
                log.info("[Fetched] response data: " + response.getData());
            }

            return userObject;
        }catch (HttpClientErrorException e){
            log.error(e.getMessage());
            return null;
        }
    }

    public SignUpRespone saveUser(SignupRequest user){
        String signUpUrl = userServiceHost + "api/v1/users/save";
        try {
            ResDTO<?> response = restTemplate.postForObject(signUpUrl, user, ResDTO.class);
            SignUpRespone responseObject = new SignUpRespone();
            if(response != null && response.getData() != null){
                responseObject = objectMapper.convertValue(response.getData(), SignUpRespone.class);
                log.info("[Fetched] response data: " + response.getData());
            }

            return responseObject;
        }catch (HttpClientErrorException e){
            log.error(e.getMessage());
            return null;
        }
    }
}

