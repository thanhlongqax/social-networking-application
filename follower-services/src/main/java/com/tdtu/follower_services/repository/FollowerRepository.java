package com.tdtu.follower_services.repository;

import com.tdtu.follower_services.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, String> {
    List<Follower> findByFollowerId(String followerId);
    List<Follower> findByFollowingId(String followingId);

    Follower findByFollowerIdAndFollowingIdAndIsFollowingTrue(String followerId, String followingId);
    Follower findByFollowerIdAndFollowingIdAndIsFollowingFalse(String followerId, String followingId);

    @Query("SELECT COUNT(f) FROM Follower f WHERE f.followingId = :userId")
    Long countFollowers(@Param("userId") String userId);
    @Query("SELECT COUNT(f) FROM Follower f WHERE f.followerId = :userId")
    Long countFollowing(@Param("userId") String userId);


}
