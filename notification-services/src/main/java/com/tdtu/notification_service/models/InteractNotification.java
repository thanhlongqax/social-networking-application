package com.tdtu.notification_service.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InteractNotification {
    @Id
    @Indexed
    private String id;
    private String userFullName;
    private String avatarUrl;
    private String content;
    private String title;
    private String postId;
    private String userId;
}
