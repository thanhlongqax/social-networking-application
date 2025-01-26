package com.tdtu.follower_services.producer;

import com.tdtu.follower_services.dto.respone.FollowerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowerEventProducer {
    @Value("${spring.kafka.topic.follower.name}")
    private String followerTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;


//    public FollowerEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }

    public void sendFollowEvent(FollowerResponse respone) {
        kafkaTemplate.send(followerTopic , respone);
    }

    public void sendUnfollowEvent(FollowerResponse respone) {
        kafkaTemplate.send(followerTopic , respone);
    }
//    public void sendCountFollower(ResDTO<Long> countFollower) {
//        kafkaTemplate.send(followerTopic , countFollower);
//    }
//    public void sendCountFollowing(ResDTO<Long> countFollowing) {
//        kafkaTemplate.send(followerTopic , countFollowing);
//    }


}
