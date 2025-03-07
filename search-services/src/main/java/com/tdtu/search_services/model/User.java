package com.tdtu.search_services.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String email;
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String username;
    @Nullable
    private String middleName;
    @JsonIgnore
    private String normalizedName;
    @Nullable
    private String gender;
    @Nullable
    private String profilePicture;
    @Nullable
    private String cover;
    @Nullable
    private String bio;
    private String phoneNumber;

    private String notificationKey;
    @Nullable
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime birthday;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private boolean active;

    public String getUserFullName(){
        return String.join(" ", this.getFirstName(), this.getMiddleName(), this.getLastName());
    }
}