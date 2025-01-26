package com.tdtu.user_services.producer;


import com.tdtu.user_services.dto.respone.FriendRequestNoti;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendKafkaMsgService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void pushFriendRequestNoti(FriendRequestNoti notification){
        kafkaTemplate.send("friend-request", notification);
    }
}
