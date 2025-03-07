package com.tdtu.user_services.service.Impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.tdtu.user_services.dto.request.FCMRegistrationIdsBody;
import com.tdtu.user_services.enums.ERIDHandleType;
import com.tdtu.user_services.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {
    @Value("${fcm.sender.id}")
    private String projectId;
    private final static String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";
    public String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("service-account.json").getInputStream())
                    .createScoped(Arrays.asList(SCOPES));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            log.info(e.getMessage());
            return "";
        }
    }

    public String saveUserDeviceGroup(User user, List<String> registrationIds){
        String notificationKey = user.getNotificationKey();

        //If user already have notification key, then add
        if(notificationKey != null && !notificationKey.isEmpty()){
            return handleRegistrationIds(ERIDHandleType.TYPE_ADD, user.getId(), registrationIds, notificationKey);
        }

        user.setNotificationKey(handleRegistrationIds(ERIDHandleType.TYPE_CREATE, user.getId(), registrationIds, ""));
        log.info(user.getNotificationKey());

        return user.getNotificationKey();
    }

    private String handleRegistrationIds(ERIDHandleType type, String notiKeyName, List<String> regisIds, String notificationKey){
        log.info(notificationKey);

        String notificationUrl = "https://fcm.googleapis.com/fcm/notification";
        String serverKey = getAccessToken();

        FCMRegistrationIdsBody requestBody = new FCMRegistrationIdsBody();
        requestBody.setRegistration_ids(regisIds);
        requestBody.setNotification_key_name(notiKeyName);
        requestBody.setOperation(type.getName());

        if(type == ERIDHandleType.TYPE_ADD || type == ERIDHandleType.TYPE_REMOVE){
            requestBody.setNotification_key(notificationKey);
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            log.info("Project id: " + projectId);
            log.info("Access token: " + serverKey);
            HttpPost httpPost = new HttpPost(notificationUrl);
            httpPost.setHeader("Authorization", "Bearer " + serverKey);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("project_id", projectId);
            httpPost.setHeader("access_token_auth", true);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(requestBody);

            httpPost.setEntity(new StringEntity(jsonBody));

            String responseBody = httpClient.execute(httpPost, httpResponse ->
                    EntityUtils.toString(httpResponse.getEntity()));

            JsonNode root = objectMapper.readTree(responseBody);
            log.error("Notification request sent successfully");
            log.info("Response body: "+ responseBody);

            String newNotificationKey = root.path("notification_key").asText();
            if(newNotificationKey.isEmpty()){
                log.error("Failed to send notification request: " + root.path("error").asText());
                return "";
            }
            return newNotificationKey;

        } catch (Exception e) {
            log.error("Failed to send notification request", e);
            return "";
        }
    }
}