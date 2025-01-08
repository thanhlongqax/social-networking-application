package com.tdtu.interaction_services.mapper;


import com.tdtu.interaction_services.dtos.respone.ReactResponse;
import com.tdtu.interaction_services.models.Reactions;
import com.tdtu.interaction_services.models.User;
import com.tdtu.interaction_services.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReactResponseMapper {

    public ReactResponse mapToDto(String userId, Reactions reaction, List<User> users){
        ReactResponse response = new ReactResponse();

        response.setId(reaction.getId());
        response.setMine(userId.equals(reaction.getUserId()));
        response.setType(reaction.getType());
        response.setCreatedAt(DateUtils.localDateTimeToDate(reaction.getCreatedAt()));
        response.setUser(users.stream()
                .filter(user -> user.getId().equals(reaction.getUserId()))
                .findFirst()
                .orElse(null));

        return response;
    }
}