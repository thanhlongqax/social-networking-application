package com.tdtu.interaction_services.producer;


import com.tdtu.interaction_services.dtos.respone.InteractNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class SendKafkaMsgService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topic.interact-noti.name}")
    private String notificationTopic;

    public void publishInteractNoti(InteractNotification notification){
        kafkaTemplate.send(notificationTopic, notification);
    }
}