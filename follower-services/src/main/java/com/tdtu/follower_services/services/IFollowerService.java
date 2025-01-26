package com.tdtu.follower_services.services;


import com.tdtu.follower_services.dto.ResDTO;
import com.tdtu.follower_services.dto.request.FollowerRequest;

public interface IFollowerService {

    public ResDTO<?> saveFollow(String idUser, FollowerRequest request);

    ResDTO<?> unFollow(String idUser,FollowerRequest request);
    public ResDTO<?> getFollowerCount(String token);
    public ResDTO<?> getFollowingCount(String token);
}