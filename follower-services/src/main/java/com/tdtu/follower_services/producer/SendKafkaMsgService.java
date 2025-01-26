package com.tdtu.follower_services.producer;



import com.tdtu.follower_services.dto.respone.FollowerNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendKafkaMsgService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topic.follower-noti.name}")
    private String notificationTopic;

    public void publishInteractNoti(FollowerNotification notification){
        kafkaTemplate.send(notificationTopic, notification);
    }
}