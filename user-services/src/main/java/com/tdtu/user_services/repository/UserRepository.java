package com.tdtu.user_services.repository;

import com.tdtu.user_services.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User,String> {
    List<User> findByActive(boolean active);
    Optional<User> findByIdAndActive(String id, boolean active);
    Optional<User> findByUsernameAndActive(String username, boolean active);
    List<User> findByIdInAndActive(List<String> id, boolean active);
    Boolean existsByEmailAndActive(String email, boolean active);
    Boolean existsByEmail(String email);
    Optional<User> findByEmailAndActive(String email, boolean active);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.notificationKey = :notificationKey WHERE u.id = :userId")
    void updateNotificationKey(@Param("userId") String userId, @Param("notificationKey") String notificationKey);

    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.middleName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchUsers(@Param("keyword") String keyword);
}
