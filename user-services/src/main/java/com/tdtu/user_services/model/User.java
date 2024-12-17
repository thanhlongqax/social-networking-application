package com.tdtu.user_services.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tdtu.user_services.enums.EUserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String username;
    @JsonIgnore
    private String hashPassword;
    private String firstName;
    private String lastName;
    private String middleName;
    @JsonIgnore
    private String normalizedName;
    private String gender;
    private String avatar_url;
    private String cover;
    private String bio;
    private String notificationKey;
    @JsonFormat (pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private boolean active;
    private EUserRole role;
    public String getUserFullName(){
        return String.join(" ", this.getFirstName(), this.getMiddleName(), this.getLastName());
    }
}
