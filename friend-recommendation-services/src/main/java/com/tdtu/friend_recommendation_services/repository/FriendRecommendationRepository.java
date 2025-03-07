package com.tdtu.friend_recommendation_services.repository;

import com.tdtu.friend_recommendation_services.model.FriendRecommendation;
import com.tdtu.friend_recommendation_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRecommendationRepository extends JpaRepository<FriendRecommendation,String > {
    List<FriendRecommendation> findByUserOrderByInterestScoreDesc(User user);
}
