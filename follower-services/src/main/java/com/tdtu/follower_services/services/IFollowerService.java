package com.tdtu.follower_services.services;


import com.tdtu.follower_services.dto.ResDTO;
import com.tdtu.follower_services.dto.request.FollowerRequest;

import java.util.List;

public interface IFollowerService {

    public ResDTO<?> saveFollow(String token, FollowerRequest request);

    ResDTO<?> unFollow(String token,FollowerRequest request);
    public ResDTO<?> getFollowerCount(String token , String id, String search);
    public ResDTO<?> getFollowingCount(String token ,String id, String search);
    public ResDTO<?> findFollowedUserIdsByToken(String token);
    public ResDTO<Boolean> isFollowing(String token, String targetUserId);

}