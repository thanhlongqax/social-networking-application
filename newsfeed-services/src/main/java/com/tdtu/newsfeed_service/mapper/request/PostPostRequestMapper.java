package com.tdtu.newsfeed_service.mapper.request;



import com.tdtu.newsfeed_service.dtos.request.CreatePostRequest;
import com.tdtu.newsfeed_service.enums.EPostType;
import com.tdtu.newsfeed_service.enums.EPrivacy;
import com.tdtu.newsfeed_service.models.Post;
import com.tdtu.newsfeed_service.utils.StringUtils;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class PostPostRequestMapper {

    public Post  mapToObject(CreatePostRequest request){
        Post post = new Post();

        post.setContent(request.getContent());
        post.setActive(true);


        post.setImageUrls(request.getImageUrls());
        post.setVideoUrls(request.getVideoUrls());
        post.setPrivacy(request.getPrivacy() != null ? request.getPrivacy() : EPrivacy.PUBLIC);
        post.setImageUrls(request.getImageUrls() != null ? request.getImageUrls() : new ArrayList<>());
        post.setVideoUrls(request.getVideoUrls() != null ? request.getVideoUrls() : new ArrayList<>()) ;
        post.setType(request.getType() != null ? request.getType() : EPostType.NORMAL);


        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setNormalizedContent(StringUtils.toSlug(post.getContent()));

        return post;
    }
}
