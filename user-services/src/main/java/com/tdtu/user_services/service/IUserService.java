package com.tdtu.user_services.service;

import com.tdtu.user_services.dto.respone.ResDTO;
import com.tdtu.user_services.model.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public interface IUserService {
    ResDTO<?> createUser(User user);
    ResDTO<?> updateUser(String userId, User updatedUser);
    ResDTO<?> deleteUser(String userId);
    ResDTO<?> getUserById(String userId);
}
