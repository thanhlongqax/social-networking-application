package com.tdtu.follower_services.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User implements Serializable {
    private String id;
    private String email;
    private String firstName;
    private String middleName;
    private String username;
    private String lastName;
    private String profilePicture;
    private String createdAt;
    private String userFullName;
    private String notificationKey;
}

