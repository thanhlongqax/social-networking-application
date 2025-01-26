package com.tdtu.newsfeed_service.dtos.request;



import com.tdtu.newsfeed_service.enums.EPostType;
import com.tdtu.newsfeed_service.enums.EPrivacy;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePostRequest {
    private String content;
    @Nullable
    private List<String> imageUrls;
    @Nullable
    private List<String> videoUrls;
    private EPrivacy privacy;
    private Boolean active;
    private EPostType type;
    @Nullable
    private List<PostTagReqDTO> postTags;
}