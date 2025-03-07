package com.tdtu.friend_recommendation_services.service;

import com.tdtu.friend_recommendation_services.dto.ResDTO;
import com.tdtu.friend_recommendation_services.model.User;

public interface IUserSearchService {
    public ResDTO<?> saveUserToElasticSearch(User user);
}
