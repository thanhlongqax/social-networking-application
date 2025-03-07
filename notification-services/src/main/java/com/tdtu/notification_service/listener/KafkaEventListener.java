package com.tdtu.notification_service.listener;



import com.tdtu.notification_service.dtos.FriendRequestNoti;
import com.tdtu.notification_service.dtos.Message;
import com.tdtu.notification_service.models.InteractNotification;
import com.tdtu.notification_service.services.Impl.FirebaseServiceImpl;
import com.tdtu.notification_service.services.Impl.PostServiceImpl;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaEventListener {
    private final FirebaseServiceImpl firebaseServiceImpl;
    private final PostServiceImpl postServiceImpl;
    @KafkaListener(groupId = "InteractNotification", topics = "interact-noti")
    public void consumeInteractTopic(InteractNotification notification){
        log.info("Interaction message: " + notification.toString());

        String userId = postServiceImpl.getUserIdByPostId(notification.getPostId());
        notification.setUserId(userId);
        boolean sendResult = firebaseServiceImpl.sendInteractNotification(userId, notification);
        if (sendResult)
            log.info("Message sent to target user");
        else
            log.info("Can not send message to the target user");
    }

    @KafkaListener(groupId = "ChattingNotification", topics = "chatting")
    public void consumeChattingTopic(Message message){
        log.info("Chatting message: " + message.toString());
        if(firebaseServiceImpl.sendChatNotification(message)){
            log.info("Message sent to target user");
        }else{
            log.info("Can not send message to the target user");
        }
    }

    @KafkaListener(groupId = "FriendRequestNotification", topics = "friend-request")
    public void consumeFriendRequest(FriendRequestNoti message){
        log.info("Friend request message: " + message.toString());
        if(firebaseServiceImpl.sendFriendRequestNotification(message)){
            log.info("Message sent to target user");
        }else{
            log.info("Message sent to target user");
        }
    }
}