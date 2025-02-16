package com.tdtu.follower_services.services;


import com.tdtu.follower_services.dto.ResDTO;
import com.tdtu.follower_services.dto.request.FollowerRequest;

public interface IFollowerService {

    public ResDTO<?> saveFollow(String token, FollowerRequest request);

    ResDTO<?> unFollow(String token,FollowerRequest request);
    public ResDTO<?> getFollowerCount(String token , String search);
    public ResDTO<?> getFollowingCount(String token , String search);
}