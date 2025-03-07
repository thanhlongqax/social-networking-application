package com.tdtu.friend_recommendation_services.mapper.request;

import com.tdtu.friend_recommendation_services.dto.request.SaveUserReqDTO;
import com.tdtu.friend_recommendation_services.enums.EUserRole;
import com.tdtu.friend_recommendation_services.model.User;
import com.tdtu.friend_recommendation_services.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SaveUserReqMapper {
    public User mapToObject(SaveUserReqDTO dto){
        User user = new User();

        user.setActive(true);
        user.setRole(EUserRole.ROLE_USER);

        user.setEmail(dto.getEmail());
        user.setHashPassword(dto.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setFirstName(dto.getFirstName() != null ? dto.getFirstName() : "");
        user.setMiddleName(dto.getMiddleName() != null ? dto.getMiddleName() : "");
        user.setLastName(dto.getLastName() != null ? dto.getLastName() : "");
        user.setUsername(dto.getUsername());
        user.setBio(dto.getBio() != null ? dto.getBio() : "");
        user.setProfilePicture(dto.getProfilePicture() != null ? dto.getProfilePicture() : "");
        user.setGender(dto.getGender() != null ? dto.getGender() : "UNKNOWN");
        user.setNormalizedName(StringUtils.toSlug(user.getFirstName().concat(user.getMiddleName()).concat(user.getLastName())));
        return user;
    }
}
