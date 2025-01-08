package com.tdtu.newsfeed_service.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
public class PostShare {
    @Id
    private String id;
    private String status;
    private LocalDateTime sharedAt;
    private String sharedUserId;
    private String sharedPostId;
}