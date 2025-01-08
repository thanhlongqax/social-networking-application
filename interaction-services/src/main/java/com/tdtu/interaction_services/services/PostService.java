package com.tdtu.interaction_services.services;

import com.tdtu.interaction_services.models.Post;
import org.springframework.stereotype.Service;

public interface PostService {
    public Post findById(String token, String postId);
}
