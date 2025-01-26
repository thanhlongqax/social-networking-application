package com.tdtu.follower_services.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerNotification {
    private String userId;
    private String content;
    private String title;
    private Date Timestamp;
    private boolean isRead;
}