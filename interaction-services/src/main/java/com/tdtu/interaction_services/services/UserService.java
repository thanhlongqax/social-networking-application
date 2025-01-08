package com.tdtu.interaction_services.services;

import com.tdtu.interaction_services.models.User;
import org.springframework.stereotype.Service;

import java.util.List;
public interface UserService {
    public User findById(String userId);
    public List<User> findByIds(List<String> ids);
    public List<User> findUserFriendIdsByUserToken(String token);
}
