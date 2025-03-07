package com.tdtu.friend_recommendation_services.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    @Nullable
    private String lastName;
    @Nullable
    private String firstName;

    @Nullable
    private String gender;
    @Nullable
    private String username;
    @Nullable
    private String profilePicture;

    @Nullable
    private String bio;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime birthday;

}
