package com.tdtu.newsfeed_service.services;



import com.tdtu.newsfeed_service.models.PostShare;
import com.tdtu.newsfeed_service.repositories.CustomPostShareRepository;
import com.tdtu.newsfeed_service.repositories.PostRepository;
import com.tdtu.newsfeed_service.repositories.PostShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostShareService {
    private final PostShareRepository postShareRepository;
    private final CustomPostShareRepository customPostShareRepository;
    private final PostRepository postRepository;

    public List<PostShare> findSharedPostByUserId(String userId){
        return postShareRepository.findBySharedUserId(userId);
    }

    public PostShare save(PostShare postShare){
        return postShareRepository.save(postShare);
    }

    public List<PostShare> findSharedPostByFriendIds(List<String> friendIds, String userId, LocalDateTime startTime){
        return customPostShareRepository.findSharedPostIdsByFriends(friendIds, userId, startTime);
    }
}