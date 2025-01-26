package com.tdtu.chat_services.service;

import com.tdtu.chat_services.model.User;

public interface IUserService {
    public User findById(String userId);

}
