package com.tdtu.follower_services.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Follower {
    @Id
    private String id;

    private String followerUserId; // ID của người theo dõi
    private String followingUserId; // ID của người được theo dõi
    private Boolean activeFollow = true; // Trạng thái theo dõi
}
