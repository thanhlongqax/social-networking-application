package com.tdtu.interaction_services.mapper;

import com.tdtu.interaction_services.dtos.request.DoReactRequest;
import com.tdtu.interaction_services.models.Reactions;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DoReactMapper {
    public Reactions mapToObject(String userId, DoReactRequest request){
        Reactions reaction = new Reactions();

        reaction.setType(request.getType());
        reaction.setCreatedAt(LocalDateTime.now());
        reaction.setPostId(request.getPostId());
        reaction.setUserId(userId);

        return reaction;
    }
}