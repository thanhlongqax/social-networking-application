package com.tdtu.newsfeed_service.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.newsfeed_service.enums.EReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reacts {
    private String id;
    private EReactionType type;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdAt;
    private User user;
    private boolean isMine;
}
