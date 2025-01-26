package com.tdtu.auth_services.controller;

import com.tdtu.auth_services.dto.ResDTO;
import com.tdtu.auth_services.dto.request.SigninRequest;
import com.tdtu.auth_services.dto.request.SignupRequest;
import com.tdtu.auth_services.service.AuthService;
import com.tdtu.auth_services.service.Impl.AuthServiceImpl;
//import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
//@Tag(name = "Authentication Service", description = "API For Authentication")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody SigninRequest loginRequest){
        log.info("Incoming login request: {}", loginRequest);
        ResDTO<?> response = authService.loginUser(loginRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUpUser(@RequestBody SignupRequest signUpRequest){
        log.info("Incoming signup request: {}", signUpRequest);
        ResDTO<?> response = authService.signUpUser(signUpRequest);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
