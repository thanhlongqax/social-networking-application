package com.tdtu.friend_recommendation_services.mapper.request;

import com.tdtu.friend_recommendation_services.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class UserInteractionDTO {
    private String id;
    private User user;
    private User targetUser;
    private int likes;
    private int comments;
    private int follows;
    private int messages;
}
