package com.tdtu.notification_service.services;

import org.springframework.stereotype.Service;

@Service
public interface PostService {
    public String getUserIdByPostId(String postId);

}
