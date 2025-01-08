package com.tdtu.user_services.model;


import com.tdtu.user_services.enums.EFriendshipStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private EFriendshipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fromUserId")
    private User fromUser;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "toUserId")
    private User toUser;
}