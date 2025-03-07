package com.tdtu.interaction_services.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "post_reaction_count")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PostReactionCountMongo {
    @Id
    private String postId;
    private int likeCount;
    private int loveCount;
    private int hahaCount;
    private int wowCount;
    private int sadCount;
    private int angryCount;
}
