package com.tdtu.friend_recommendation_services.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.tdtu.friend_recommendation_services.mapper.request.UserInteractionDTO;
import com.tdtu.friend_recommendation_services.model.UserInteraction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendKafkaMsgService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendUserInteraction(UserInteractionDTO interaction) {
        try {
            kafkaTemplate.send("user_interactions", interaction);
        } catch (Exception  e) {
            log.error("Error serializing UserInteraction: {}", e.getMessage());
        }
    }

}
