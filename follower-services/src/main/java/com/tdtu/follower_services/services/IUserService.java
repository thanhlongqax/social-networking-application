package com.tdtu.follower_services.services;

import com.tdtu.follower_services.models.User;

import java.util.List;

public interface IUserService {
    public User findById(String userId);
    public List<User> findByIds(List<String> ids);
    public List<User> findUserFriendIdsByUserToken(String token);
}
