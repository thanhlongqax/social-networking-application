package com.tdtu.follower_services.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {
    private String followerId;
    private String followingId;
    private boolean IsFollowing;

}
