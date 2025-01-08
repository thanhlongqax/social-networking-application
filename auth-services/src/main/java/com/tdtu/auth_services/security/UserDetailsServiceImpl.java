package com.tdtu.auth_services.security;

import com.tdtu.auth_services.model.User;
import com.tdtu.auth_services.service.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserServiceImpl userServiceImpl;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = userServiceImpl.getUserInfo(email);
        if(foundUser == null)
            throw new UsernameNotFoundException("User not found with username: " + email);
        return UserDetailsImpl.build(foundUser);
    }
}