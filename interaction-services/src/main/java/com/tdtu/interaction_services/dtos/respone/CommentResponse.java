package com.tdtu.interaction_services.dtos.respone;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.interaction_services.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentResponse {
    private String id;
    private String content;
    private String parentId;
    private List<String> imageUrls;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date updatedAt;
    private User user;
    private List<CommentResponse> children;
    private boolean isMine;
}