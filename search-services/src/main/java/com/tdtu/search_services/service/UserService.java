package com.tdtu.search_services.service;

import com.tdtu.search_services.model.User;

import java.util.List;

public interface UserService {
    public List<User> findByIds(List<String> ids);

}
