package com.tdtu.search_services.service;

import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.dto.request.UserToElasticSearchDTO;
import com.tdtu.search_services.model.User;

import java.io.IOException;

public interface IUserSearchService {
    public ResDTO<?> searchUsers(String token , String keyword) throws IOException;
    public ResDTO<?> saveUserToElasticSearch(UserToElasticSearchDTO user);
}
