package com.tdtu.follower_services.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "followers")
@AllArgsConstructor
@NoArgsConstructor
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String followerId;
    private String followingId;
    private Boolean isFollowing = true;
}
