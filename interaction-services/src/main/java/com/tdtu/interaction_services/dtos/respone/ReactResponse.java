package com.tdtu.interaction_services.dtos.respone;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.interaction_services.enums.EReactionType;
import com.tdtu.interaction_services.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReactResponse {
    private String id;
    private EReactionType type;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdAt;
    private User user;
    private boolean isMine;
}