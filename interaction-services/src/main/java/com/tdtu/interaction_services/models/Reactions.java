package com.tdtu.interaction_services.models;


import com.tdtu.interaction_services.enums.EReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document
public class Reactions {
    @Id
    private String id;
    private EReactionType type;
    private LocalDateTime createdAt;

    private String userId;
    private String postId;
}
