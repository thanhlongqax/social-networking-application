package com.tdtu.newsfeed_service.services;


import com.tdtu.newsfeed_service.dtos.respone.InteractNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SendKafkaMsgService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void pushSharePostMessage(InteractNotification notification){
        kafkaTemplate.send("interact-noti", notification);
    }
}