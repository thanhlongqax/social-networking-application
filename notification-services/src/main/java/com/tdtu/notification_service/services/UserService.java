package com.tdtu.notification_service.services;

import com.tdtu.notification_service.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User findById(String userId);
    public List<User> findByIds(List<String> ids);

}
