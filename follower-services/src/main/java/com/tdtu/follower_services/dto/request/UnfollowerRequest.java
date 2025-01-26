package com.tdtu.follower_services.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnfollowerRequest {
    private Long followerId;
    private Long followingId;

}