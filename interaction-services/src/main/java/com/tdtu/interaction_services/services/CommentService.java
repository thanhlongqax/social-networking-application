package com.tdtu.interaction_services.services;

import com.tdtu.interaction_services.dtos.ResDTO;
import com.tdtu.interaction_services.dtos.request.AddCommentRequest;
import com.tdtu.interaction_services.dtos.request.UpdateCommentRequest;
import org.springframework.stereotype.Service;

public interface CommentService {
    public ResDTO<?> addComment(String token, AddCommentRequest request);
    public ResDTO<?> updateComment(String token, String id, UpdateCommentRequest comment);
    public ResDTO<?> deleteComment(String token, String id);
    public ResDTO<?> findCommentById(String token, String id);
    public ResDTO<?> findCommentsByPostId(String token, String postId);
    public ResDTO<?> findAllComments(String token);
}
