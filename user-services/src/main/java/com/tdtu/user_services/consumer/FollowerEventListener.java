package com.tdtu.user_services.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FollowerEventListener {

    @KafkaListener(topics = "follower-topic", groupId = "follower-group")
    public void receiveFollow(){
        System.out.println("Đã nhận message");
    }

}
