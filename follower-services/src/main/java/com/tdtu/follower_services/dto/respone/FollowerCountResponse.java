package com.tdtu.follower_services.dto.respone;

import com.tdtu.follower_services.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerCountResponse {
    private Long countFollower;
    private List<User> user;
}
