package com.tdtu.friend_recommendation_services.repository;

import com.tdtu.friend_recommendation_services.model.User;
import com.tdtu.friend_recommendation_services.model.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest,String> {
    List<UserInterest> findByUser(User user);
}
