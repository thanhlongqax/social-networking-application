package com.tdtu.notification_service.services.Impl;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.tdtu.notification_service.dtos.*;
import com.tdtu.notification_service.models.InteractNotification;
import com.tdtu.notification_service.models.User;
import com.tdtu.notification_service.services.FireBaseService;
import com.tdtu.notification_service.services.InteractNotiService;
import com.tdtu.notification_service.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FirebaseServiceImpl implements FireBaseService {

    private final UserServiceImpl userServiceImpl;
    private final InteractNotiService notiService;

    @Autowired
    public FirebaseServiceImpl(InteractNotiServiceImpl notiService,UserServiceImpl userServiceImpl){
        this.notiService = notiService;
        this.userServiceImpl= userServiceImpl;
    }
    private final static String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";
    @Value("${fcm.project.id}")
    private String projectId;
    public String getAccessToken() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource("service-account.json").getInputStream())
                    .createScoped(List.of(SCOPES));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();

        } catch (IOException e) {
            log.info(e.getMessage());
            return "";
        }
    }

    public boolean sendInteractNotification(String userId, InteractNotification interactNotification){
        String SEND_NOTI_URL = "https://fcm.googleapis.com/v1/projects/"+ projectId +"/messages:send";

        User foundUser = userServiceImpl.findById(userId);

        if (foundUser != null) {

            String notificationKey = foundUser.getNotificationKey();
            if(notificationKey == null || notificationKey.isEmpty()){
                return false;
            }
            String token = getAccessToken();
            try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                HttpPost httpPost = new HttpPost(SEND_NOTI_URL);
                httpPost.setHeader("Authorization", "Bearer " + token);
                httpPost.setHeader("Content-Type", "application/json");

                ObjectMapper objectMapper = new ObjectMapper();
                NotificationRequestBody<InteractNotification> requestBody = new NotificationRequestBody<>();
                NotificationMessage<InteractNotification> message = new NotificationMessage<>();
                requestBody.setMessage(message);

                NotificationContent notificationContent = new NotificationContent();
                notificationContent.setTitle("Có người tương tác nè!!");
                notificationContent.setBody(interactNotification.getContent());
                notificationContent.setImage(interactNotification.getAvatarUrl());

                message.setNotification(notificationContent);
                message.setData(interactNotification);
                message.setToken(notificationKey);

                String jsonBody = objectMapper.writeValueAsString(requestBody);

                log.info("Request body: " + jsonBody);

                httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

                return httpClient.execute(httpPost, response -> {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == 200) {
                        log.info("Notification sent successfully.");

                        notiService.save(interactNotification);

                        return true;
                    } else {
                        String responseBody = EntityUtils.toString(response.getEntity());
                        log.info("Failed to send notification. Status code: " + status);
                        log.info("Response Body: " + responseBody);
                        return false;
                    }
                });
            } catch (Exception e) {
                log.error("Error sending notification", e);
                return false;
            }
        }

        return false;
    }

    public boolean sendChatNotification(Message message){
        String SEND_NOTI_URL = "https://fcm.googleapis.com/v1/projects/"+ projectId +"/messages:send";

        String toUserId = message.getToUserId();
        String fromUserId = message.getFromUserId();

        List<User> users = userServiceImpl.findByIds(List.of(message.getToUserId(), message.getFromUserId()));
        if (users != null && !users.isEmpty()) {
            User toUser = users.stream().filter(user -> user.getId().equals(toUserId)).findFirst().orElse(null);
            User fromUser = users.stream().filter(user -> user.getId().equals(fromUserId)).findFirst().orElse(null);

            if(toUser != null){
                String notificationKey = toUser.getNotificationKey();
                if(notificationKey == null || notificationKey.isEmpty()){
                    return false;
                }
                String token = getAccessToken();

                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpPost httpPost = new HttpPost(SEND_NOTI_URL);
                    httpPost.setHeader("Authorization", "Bearer " + token);
                    httpPost.setHeader("Content-Type", "application/json");

                    ObjectMapper objectMapper = new ObjectMapper();
                    NotificationRequestBody<Message> requestBody = new NotificationRequestBody<>();
                    NotificationMessage<Message> notificationMessage = new NotificationMessage<>();
                    requestBody.setMessage(notificationMessage);

                    NotificationContent notificationContent = new NotificationContent();
                    notificationContent.setTitle(getUserFullName(fromUser));
                    notificationContent.setBody(message.getContent());
                    notificationContent.setImage(getUserAvatar(fromUser));

                    NewMessageNoti messageNoti = new NewMessageNoti();
                    messageNoti.setId(message.getId());
                    messageNoti.setTitle(getUserFullName(fromUser));
                    messageNoti.setContent(message.getContent());
                    messageNoti.setCreatedAt(message.getCreatedAt());
                    messageNoti.setImageUrls(message.getImageUrls());
                    messageNoti.setFromUserId(message.getFromUserId());
                    messageNoti.setToUserId(message.getToUserId());

                    notificationMessage.setNotification(notificationContent);
                    notificationMessage.setData(messageNoti);
                    notificationMessage.setToken(notificationKey);

                    String jsonBody = objectMapper.writeValueAsString(requestBody);

                    log.info("Request body: " + jsonBody);

                    httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

                    return httpClient.execute(httpPost, response -> {
                        int status = response.getStatusLine().getStatusCode();
                        if (status == 200) {
                            log.info("Notification sent successfully.");
                            return true;
                        } else {
                            String responseBody = EntityUtils.toString(response.getEntity());
                            log.info("Failed to send notification. Status code: " + status);
                            log.info("Response Body: " + responseBody);
                            return false;
                        }
                    });
                } catch (Exception e) {
                    log.error("Error sending notification", e);
                    return false;
                }
            }else{
                log.error("Failed to send to null user");
            }
        }

        return false;
    }

    public boolean sendFriendRequestNotification(FriendRequestNoti message){
        String SEND_NOTI_URL = "https://fcm.googleapis.com/v1/projects/"+ projectId +"/messages:send";

        String notificationKey = message.getNotificationKey();

        if(notificationKey == null || notificationKey.isEmpty()){
            return false;
        }

        String token = getAccessToken();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SEND_NOTI_URL);
            httpPost.setHeader("Authorization", "Bearer " + token);
            httpPost.setHeader("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            NotificationRequestBody<FriendRequestNoti> requestBody = new NotificationRequestBody<>();
            NotificationMessage<FriendRequestNoti> notificationMessage = new NotificationMessage<>();
            requestBody.setMessage(notificationMessage);

            NotificationContent notificationContent = new NotificationContent();
            notificationContent.setTitle("Yêu cầu kết bạn");
            notificationContent.setBody(message.getMessage());
            notificationContent.setImage(message.getAvatarUrl());

            notificationMessage.setNotification(notificationContent);
            notificationMessage.setData(message);
            notificationMessage.setToken(notificationKey);

            String jsonBody = objectMapper.writeValueAsString(requestBody);

            log.info("Request body: " + jsonBody);

            httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));

            return httpClient.execute(httpPost, response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    log.info("Notification sent successfully.");
                    return true;
                } else {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    log.info("Failed to send notification. Status code: " + status);
                    log.info("Response Body: " + responseBody);
                    return false;
                }
            });
        } catch (Exception e) {
            log.error("Error sending notification", e);
            return false;
        }
    }

    private String getUserFullName(User foundUser){
        return foundUser != null ? String.join(" ", foundUser.getFirstName(), foundUser.getMiddleName(), foundUser.getLastName()) : "Unknown";
    }

    private String getUserAvatar(User foundUser){
        return foundUser != null ? foundUser.getProfilePicture() : "Unknown";
    }
}