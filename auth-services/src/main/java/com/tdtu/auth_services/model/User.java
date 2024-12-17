package com.tdtu.auth_services.model;

import com.tdtu.auth_services.enums.EUserRole;
import com.tdtu.auth_services.enums.EUserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private String id;
    private String email;
    private String userFullName;
    private String password;
    private EUserRole role;
    private EUserStatus status;
}
