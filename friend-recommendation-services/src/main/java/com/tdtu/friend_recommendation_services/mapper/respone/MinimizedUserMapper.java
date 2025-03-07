package com.tdtu.friend_recommendation_services.mapper.respone;


import com.tdtu.friend_recommendation_services.dto.respone.MinimizedUserResponse;
import com.tdtu.friend_recommendation_services.model.User;
import org.springframework.stereotype.Component;

@Component
public class MinimizedUserMapper {
    public MinimizedUserResponse mapToDTO(User user){
        if(user != null){
            MinimizedUserResponse minimizedUser = new MinimizedUserResponse();

            minimizedUser.setId(user.getId());
            minimizedUser.setUserFullName(user.getUserFullName());
            minimizedUser.setUsername(user.getUsername());
            minimizedUser.setEmail(user.getEmail());
            minimizedUser.setCreatedAt(user.getCreatedAt());
            minimizedUser.setFirstName(user.getFirstName());
            minimizedUser.setMiddleName(user.getMiddleName());
            minimizedUser.setLastName(user.getLastName());
            minimizedUser.setProfilePicture(user.getProfilePicture() != null ? user.getProfilePicture() : "");
            minimizedUser.setNotificationKey(user.getNotificationKey());

            return minimizedUser;
        }

        return null;
    }
}
