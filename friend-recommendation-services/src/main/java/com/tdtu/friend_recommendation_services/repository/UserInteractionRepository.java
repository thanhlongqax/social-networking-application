package com.tdtu.friend_recommendation_services.repository;

import com.tdtu.friend_recommendation_services.model.User;
import com.tdtu.friend_recommendation_services.model.UserInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserInteractionRepository extends JpaRepository<UserInteraction , String> {
    List<UserInteraction> findByUserAndTargetUser(User user, User targetUser);

    // Lấy tất cả người dùng ngoại trừ người dùng hiện tại (tìm bạn bè tiềm năng)
    @Query("SELECT u FROM User u WHERE u.id <> :userId")
    List<User> findAllUsersExcept(@Param("userId") String userId);
}
