package com.tdtu.notification_service.services;

import com.tdtu.notification_service.dtos.FriendRequestNoti;
import com.tdtu.notification_service.dtos.Message;
import com.tdtu.notification_service.models.InteractNotification;
import com.tdtu.notification_service.models.User;
import org.springframework.stereotype.Service;

@Service
public interface FireBaseService {
    public String getAccessToken();
    public boolean sendInteractNotification(String userId, InteractNotification interactNotification);
    public boolean sendChatNotification(Message message);
    public boolean sendFriendRequestNotification(FriendRequestNoti message);

}
