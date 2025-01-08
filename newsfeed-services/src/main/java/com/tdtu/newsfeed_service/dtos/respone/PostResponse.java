package com.tdtu.newsfeed_service.dtos.respone;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.newsfeed_service.enums.EPostType;
import com.tdtu.newsfeed_service.enums.EPrivacy;
import com.tdtu.newsfeed_service.enums.EReactionType;
import com.tdtu.newsfeed_service.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse implements Serializable {
    private String id;
    private String content;
    private List<String> imageUrls;
    private List<String> videoUrls;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date updatedAt;
    private EPrivacy privacy;
    private EPostType type;
    private User user;
    private int noShared;
    private int noComments;
    private int noReactions;
    private List<TopReacts> topReacts;
    private EReactionType reacted;
    private ShareInfo shareInfo;
    private List<User> taggedUsers;
    private boolean isMine;
}