package com.tdtu.auth_services.service;

import com.tdtu.auth_services.dto.request.SignupRequest;
import com.tdtu.auth_services.dto.respone.SignUpRespone;
import com.tdtu.auth_services.model.User;
import org.springframework.stereotype.Service;

public interface UserService {
    public User getUserInfo(String email);
    public SignUpRespone saveUser(SignupRequest user);
}
