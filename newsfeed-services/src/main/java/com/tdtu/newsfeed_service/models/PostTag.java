package com.tdtu.newsfeed_service.models;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostTag {
    private String id;
    private LocalDateTime createdAt;
    private User taggedUser;
}
