package com.tdtu.friend_recommendation_services.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.friend_recommendation_services.model.FriendRecommendation;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaFriendRecommendationConsumer {

//    private final ElasticsearchService elasticsearchService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "friend_recommendations", groupId = "recommendation-group")
    public void consumeFriendRecommendations(FriendRecommendation friendRecommendation) {

//        elasticsearchService.saveRecommendation(recommendation);
    }
}

