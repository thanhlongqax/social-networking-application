package com.tdtu.auth_services.service.Impl;

import com.tdtu.auth_services.dto.ResDTO;
import com.tdtu.auth_services.dto.request.SigninRequest;
import com.tdtu.auth_services.dto.request.SignupRequest;
import com.tdtu.auth_services.dto.respone.SignUpRespone;
import com.tdtu.auth_services.dto.respone.SigninRespone;
import com.tdtu.auth_services.model.User;
import com.tdtu.auth_services.service.AuthService;
import com.tdtu.auth_services.service.UserService;
import com.tdtu.auth_services.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final DaoAuthenticationProvider authenticationProvider;
    private final JwtUtils jwtUtils;
    private final UserService userServiceImpl;
    private final PasswordEncoder passwordEncoder;

    public ResDTO<?> loginUser(SigninRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try{
            log.info("Password: " + password);
            log.info("Username: " + email);
            Authentication authentication = authenticationProvider.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            if(authentication.isAuthenticated()){
                User foundUser = userServiceImpl.getUserInfo(email);

                ResDTO<SigninRespone> responseDto = new ResDTO<>();
                responseDto.setCode(HttpServletResponse.SC_OK);
                responseDto.setMessage("User login successfully");
                responseDto.setData(
                        SigninRespone.builder()
                                .id(foundUser.getId())
                                .username(foundUser.getEmail())
                                .token(jwtUtils.generateJwtToken(foundUser.getId()))
                                .tokenType("Bearer")
                                .userFullName(foundUser.getUserFullName())
                                .build()
                );
                return responseDto;
            }
        }catch (AuthenticationException e){
            log.error(e.getMessage());
        }

        ResDTO<?> responseDto = new ResDTO<>();
        responseDto.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        responseDto.setMessage("Sai tên đăng nhập hoặc mật khẩu!");
        responseDto.setData(null);
        return responseDto;
    }

    public ResDTO<?> signUpUser(SignupRequest request){
        ResDTO<SignUpRespone> response = new ResDTO<>();
        request.setPassword(passwordEncoder.encode(request.getPassword()));

        SignUpRespone data = userServiceImpl.saveUser(request);

        response.setData(data);
        response.setMessage(data != null ? "Đăng ký tài khoản thành công" : "Email đã tồn tại!");
        response.setCode(data != null ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);

        return response;
    }
}
