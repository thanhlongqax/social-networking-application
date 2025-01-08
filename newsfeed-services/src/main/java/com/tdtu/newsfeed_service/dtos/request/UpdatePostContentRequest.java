package com.tdtu.newsfeed_service.dtos.request;



import com.tdtu.newsfeed_service.enums.EPrivacy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePostContentRequest {
    private String id;
    private String content;
    private EPrivacy privacy;
    private List<String> taggingUsers;
    private List<String> videoUrls;
    private List<String> imageUrls;
}
