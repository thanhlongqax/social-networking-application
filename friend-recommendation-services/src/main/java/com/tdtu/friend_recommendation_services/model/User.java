package com.tdtu.friend_recommendation_services.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tdtu.friend_recommendation_services.enums.EUserRole;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    @JsonIgnore
    private String hashPassword;
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
    private EUserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInterest> interests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserInteraction> interactions;
    @Transient
    private Long followerCount;

    @Transient
    private Long followingCount;
    public String getUserFullName(){
        return String.join(" ", this.getFirstName(), this.getMiddleName(), this.getLastName());
    }
}