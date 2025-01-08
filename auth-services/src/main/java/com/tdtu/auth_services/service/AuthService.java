package com.tdtu.auth_services.service;

import com.tdtu.auth_services.dto.ResDTO;
import com.tdtu.auth_services.dto.request.SigninRequest;
import com.tdtu.auth_services.dto.request.SignupRequest;
import org.springframework.stereotype.Service;

public interface AuthService {
    public ResDTO<?> loginUser(SigninRequest loginRequest);
    public ResDTO<?> signUpUser(SignupRequest request);
}
