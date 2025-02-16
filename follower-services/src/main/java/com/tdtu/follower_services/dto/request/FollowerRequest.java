package com.tdtu.follower_services.dto.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerRequest {
    private String userId;
    @Nullable
    private String fromTo;

}