package com.tdtu.newsfeed_service.dtos.request;



import com.tdtu.newsfeed_service.enums.EPostType;
import com.tdtu.newsfeed_service.enums.EPrivacy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePostRequest {
    private String content;
    private List<String> imageUrls;
    private List<String> videoUrls;
    private EPrivacy privacy;
    private Boolean active;
    private EPostType type;
    private List<PostTagReqDTO> postTags;
}