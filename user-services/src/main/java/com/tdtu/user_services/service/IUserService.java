package com.tdtu.user_services.service;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.model.User;

public interface IUserService {
    ResDTO<?> createUser(User user);
    ResDTO<?> updateUser(String userId, User updatedUser);
    ResDTO<?> deleteUser(String userId);
    ResDTO<?> getUserById(String userId);
}
