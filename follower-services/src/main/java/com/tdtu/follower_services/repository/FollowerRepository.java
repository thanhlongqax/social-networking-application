package com.tdtu.follower_services.repository;

import com.tdtu.follower_services.models.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends MongoRepository<Follower, String> {
//    List<Follower> findByFollowerId(String followerId);
//    List<Follower> findByFollowingId(String followingId);

    Follower findByFollowerUserIdAndFollowingUserIdAndActiveFollowTrue(String followerUserId, String followingUserId);

    Follower findByFollowerUserIdAndFollowingUserIdAndActiveFollowFalse(String followerUserId, String followingUserId);
    // Đếm số lượng follower của một user (chỉ tính những người đang theo dõi)
    Long countByFollowingUserIdAndActiveFollow(String userId, Boolean activeFollow);

    // Đếm số lượng user mà một người đang theo dõi (chỉ tính những người vẫn follow)
    Long countByFollowerUserIdAndActiveFollow(String userId, Boolean activeFollow);
    // Lấy danh sách người đang theo dõi userId (chỉ lấy những người vẫn follow)
    List<Follower> findByFollowingUserIdAndActiveFollowTrue(String userId);

    // Lấy danh sách user mà userId đang theo dõi (chỉ lấy những người vẫn follow)
    List<Follower> findByFollowerUserIdAndActiveFollowTrue(String userId);


}
