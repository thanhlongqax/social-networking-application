package com.tdtu.auth_services.dto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SigninRequest {
    private String email;
    @Nullable
    private String username;
    private String password;
}
