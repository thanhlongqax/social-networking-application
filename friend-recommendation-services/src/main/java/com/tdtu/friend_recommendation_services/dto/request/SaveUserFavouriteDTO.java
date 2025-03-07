package com.tdtu.friend_recommendation_services.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserFavouriteDTO {
    private String name;
    private String postId;
}
