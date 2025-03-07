package com.tdtu.interaction_services.dtos.respone;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractNotification {
    private String userFullName;
    private String avatarUrl;
    private String content;
    private String postId;
    private String title;
    @Nullable
    private String userId;
}