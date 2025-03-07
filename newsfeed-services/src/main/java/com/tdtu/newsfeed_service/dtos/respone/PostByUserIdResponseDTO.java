package com.tdtu.newsfeed_service.dtos.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostByUserIdResponseDTO {
    private List<PostResponse> posts;
    private int countPost;
}
