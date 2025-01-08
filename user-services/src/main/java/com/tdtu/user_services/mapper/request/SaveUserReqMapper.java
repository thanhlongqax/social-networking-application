package com.tdtu.user_services.mapper.request;

import com.tdtu.user_services.dto.request.SaveUserReqDTO;
import com.tdtu.user_services.enums.EUserRole;
import com.tdtu.user_services.model.User;
import com.tdtu.user_services.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SaveUserReqMapper {
    public User mapToObject(SaveUserReqDTO dto){
        User user = new User();

        user.setActive(true);
        user.setRole(EUserRole.ROLE_USER);
        user.setBio(dto.getBio());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setProfilePicture(dto.getProfilePicture());
        user.setHashPassword(dto.getPassword());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setNormalizedName(StringUtils.toSlug(user.getFirstName().concat(user.getMiddleName()).concat(user.getLastName())));

        return user;
    }
}
