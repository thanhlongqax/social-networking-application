package com.tdtu.user_services.dto.respone;


import com.tdtu.user_services.enums.EUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthUserResponse {
    private String id;
    private String email;
    private String password;
    private String username;
    private EUserRole role;
    private Boolean active;
    private String userFullName;
}