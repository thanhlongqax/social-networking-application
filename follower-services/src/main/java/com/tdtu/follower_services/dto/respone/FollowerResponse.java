package com.tdtu.follower_services.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {
    private String followerUserId; // ID của người theo dõi
    private String followingUserId; // ID của người được theo dõi
    private Boolean activeFollow = true; // Trạng thái theo dõi

}
