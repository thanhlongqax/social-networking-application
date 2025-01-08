package com.tdtu.newsfeed_service.mapper.request;



import com.tdtu.newsfeed_service.dtos.request.CreatePostRequest;
import com.tdtu.newsfeed_service.models.Post;
import com.tdtu.newsfeed_service.utils.StringUtils;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class PostPostRequestMapper {

    public Post  mapToObject(CreatePostRequest request){
        Post post = new Post();

        post.setContent(request.getContent());
        post.setActive(true);
        post.setPrivacy(request.getPrivacy());
        post.setImageUrls(request.getImageUrls());
        post.setVideoUrls(request.getVideoUrls());
        post.setType(request.getType());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setNormalizedContent(StringUtils.toSlug(post.getContent()));

        return post;
    }
}
