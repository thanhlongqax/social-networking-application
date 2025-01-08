package com.tdtu.auth_services.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SigninRespone {
    private String id;
    private String username;
    private String tokenType = "Bearer";
    private String token;
    private String userFullName;
}
