package com.tdtu.interaction_services.dtos.request;


import com.tdtu.interaction_services.enums.EReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoReactRequest {
    private String postId;
    private EReactionType type;
}