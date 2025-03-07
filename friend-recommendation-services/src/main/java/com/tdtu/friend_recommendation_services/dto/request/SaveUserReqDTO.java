package com.tdtu.friend_recommendation_services.dto.request;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveUserReqDTO {
    private String email;
    private String password;
    private String username;
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
    private String profilePicture;
    @Nullable
    private String bio;
}