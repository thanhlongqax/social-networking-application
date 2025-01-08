package com.tdtu.notification_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FriendRequestNoti {
    private String userFullName;
    private String avatarUrl;
    private String message;
    private String notificationKey;
    private String title;
}
