package com.tdtu.chat_services.producer;

import com.tdtu.chat_services.dto.MessageNoti;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SendKafkaMsgService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void publishMessageNoti(MessageNoti message){
        kafkaTemplate.send("chatting", message);
    }
}
