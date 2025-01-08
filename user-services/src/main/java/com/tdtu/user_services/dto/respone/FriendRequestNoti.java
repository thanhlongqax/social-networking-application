package com.tdtu.user_services.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestNoti {
    private String userFullName;
    private String avatarUrl;
    private String message;
    private String notificationKey;
    private String title;
}