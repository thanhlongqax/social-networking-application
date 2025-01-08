package com.tdtu.auth_services.controller;

import com.tdtu.auth_services.dto.ResDTO;
import com.tdtu.auth_services.dto.request.SigninRequest;
import com.tdtu.auth_services.dto.request.SignupRequest;
import com.tdtu.auth_services.service.AuthService;
import com.tdtu.auth_services.service.Impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody SigninRequest loginRequest){
        ResDTO<?> response = authService.loginUser(loginRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUser(@RequestBody SignupRequest signUpRequest){
        ResDTO<?> response = authService.signUpUser(signUpRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
