package com.tdtu.auth_services.security;


import com.tdtu.auth_services.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {
    private String id;
    private String email;
    private String password;
    private List<GrantedAuthority> roles;
    private Boolean active;
    public static UserDetailsImpl build(User user){
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.email = user.getEmail();
        userDetails.password = user.getPassword();
        userDetails.id = user.getId();
        userDetails.active = user.getActive();
        userDetails.roles = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        return userDetails;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}