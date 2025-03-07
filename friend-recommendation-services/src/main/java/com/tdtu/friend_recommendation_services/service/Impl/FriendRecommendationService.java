package com.tdtu.friend_recommendation_services.service.Impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.tdtu.friend_recommendation_services.model.FriendRecommendation;
import com.tdtu.friend_recommendation_services.model.User;
import com.tdtu.friend_recommendation_services.model.UserInteraction;
import com.tdtu.friend_recommendation_services.model.UserInterest;
import com.tdtu.friend_recommendation_services.repository.FriendRecommendationRepository;
import com.tdtu.friend_recommendation_services.repository.UserInteractionRepository;
import com.tdtu.friend_recommendation_services.repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendRecommendationService {
    private final UserInteractionRepository userInteractionRepository;
    private final UserInterestRepository userInterestRepository;
    private final FriendRecommendationRepository friendRecommendationRepository;
    private final ElasticsearchClient elasticsearchClient;
    // Công thức tính điểm tổng hợp giữa Content-Based Filtering và Collaborative Filtering
    public double calculateInterestScore(User user1, User user2) {
        // 1. Tính điểm dựa trên sở thích (Content-Based Filtering)
        double contentBasedScore = calculateContentBasedScore(user1, user2);

        // 2. Tính điểm dựa trên tương tác (Collaborative Filtering)
        double collaborativeScore = calculateCollaborativeScore(user1, user2);

        // 3. Kết hợp cả hai điểm (trọng số có thể điều chỉnh)
        return 0.6 * contentBasedScore + 0.4 * collaborativeScore;
    }

    // Tính điểm dựa trên sở thích (Content-Based Filtering)
    private double calculateContentBasedScore(User user1, User user2) {
        List<String> interests1 = userInterestRepository.findByUser(user1)
                .stream().map(UserInterest::getInterest).collect(Collectors.toList());
        List<String> interests2 = userInterestRepository.findByUser(user2)
                .stream().map(UserInterest::getInterest).collect(Collectors.toList());

        // Đếm số sở thích chung
        long commonInterests = interests1.stream().filter(interests2::contains).count();

        // Tính điểm (chuẩn hóa về thang điểm 0 - 1)
        return (double) commonInterests / Math.max(interests1.size(), interests2.size());
    }

    // Tính điểm dựa trên tương tác (Collaborative Filtering)
    private double calculateCollaborativeScore(User user1, User user2) {
        List<UserInteraction> interactions = userInteractionRepository.findByUserAndTargetUser(user1, user2);

        return interactions.stream().mapToDouble(UserInteraction::calculateInteractionScore).sum();
    }

    // Lưu kết quả vào Elasticsearch
    public void saveFriendRecommendations(User user) {
        List<User> allUsers = userInteractionRepository.findAllUsersExcept(user.getId());
        List<FriendRecommendation> recommendations = new ArrayList<>();

        for (User candidate : allUsers) {
            double score = calculateInterestScore(user, candidate);
            if (score > 0.5) { // Chỉ lưu nếu điểm cao hơn ngưỡng
                FriendRecommendation recommendation = new FriendRecommendation();
                recommendation.setUser(user);
                recommendation.setRecommendedUser(candidate);
                recommendation.setInterestScore(score);
                recommendations.add(recommendation);
            }
        }
        friendRecommendationRepository.saveAll(recommendations);
        saveRecommendations(recommendations);
    }
    private void saveRecommendations(List<FriendRecommendation> recommendations) {

        try {
            BulkRequest.Builder br = new BulkRequest.Builder();

            for (FriendRecommendation recommendation : recommendations) {
                br.operations(op -> op
                        .index(i -> i
                                .index("friend_recommendations")
                                .id(recommendation.getUser().getId())
                                .document(recommendation)
                        )
                );
            }

            elasticsearchClient.bulk(br.build());
        }catch (IOException e){
            log.info("lỗi :{}",e);
        }
    }

}
