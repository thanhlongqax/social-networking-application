package com.tdtu.auth_services.dto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {

    private String email;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String middleName;
    @Nullable
    private String gender;
    @Nullable
    private String avatar_url;
    @Nullable
    private String bio;

}
