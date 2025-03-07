package com.tdtu.user_services.service;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.model.User;

public interface IUserSearchService {
    public ResDTO<?> saveUserToElasticSearch(User user);
}
