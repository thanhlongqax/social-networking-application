package com.tdtu.interaction_services.mapper;


import com.tdtu.interaction_services.dtos.respone.CommentResponse;
import com.tdtu.interaction_services.models.Comments;
import com.tdtu.interaction_services.repositories.CommentsRepository;
import com.tdtu.interaction_services.services.Impl.UserServiceImpl;
import com.tdtu.interaction_services.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class CommentResponseMapper {
    private final UserServiceImpl userServiceImpl;
    private final CommentsRepository commentsRepository;
    public CommentResponse mapToDto(Comments comment){
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setUser(userServiceImpl.findById(comment.getUserId()));
        commentResponse.setCreatedAt(DateUtils.localDateTimeToDate(comment.getCreatedAt()));
        commentResponse.setUpdatedAt(DateUtils.localDateTimeToDate(comment.getUpdatedAt()));
        commentResponse.setContent(comment.getContent());
        commentResponse.setImageUrls(comment.getImageUrls());
        commentResponse.setParentId(comment.getParentId());
        commentResponse.setChildren(commentsRepository.findByParentId(comment.getId())
                .stream()
                .map(this::mapToChildrenCommentDTO)
                .toList());

        return commentResponse;
    }

    private CommentResponse mapToChildrenCommentDTO(Comments comment){
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setUser(userServiceImpl.findById(comment.getUserId()));
        commentResponse.setCreatedAt(DateUtils.localDateTimeToDate(comment.getCreatedAt()));
        commentResponse.setUpdatedAt(DateUtils.localDateTimeToDate(comment.getUpdatedAt()));
        commentResponse.setContent(comment.getContent());
        commentResponse.setImageUrls(comment.getImageUrls());
        commentResponse.setParentId(comment.getParentId());
        commentResponse.setChildren(new ArrayList<>());

        return commentResponse;
    }
}