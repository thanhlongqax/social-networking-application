package com.tdtu.friend_recommendation_services.service;


import com.tdtu.friend_recommendation_services.model.User;

public interface IUserService {
    public User findById(String userId);

}
