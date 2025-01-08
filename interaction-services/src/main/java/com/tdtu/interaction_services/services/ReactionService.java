package com.tdtu.interaction_services.services;

import com.tdtu.interaction_services.dtos.ResDTO;
import com.tdtu.interaction_services.dtos.request.DoReactRequest;


public interface ReactionService {
    public ResDTO<?> doReaction(String token, DoReactRequest request);
    public ResDTO<?> getReactsByPostId(String token, String postId);
}
