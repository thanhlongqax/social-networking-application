package com.tdtu.chat_services.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.chat_services.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageNoti {
    private String id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    private String content;
    private String imageUrls;
    private String fromUserId;
    private String toUserId;

    public MessageNoti(Message message){
        this.id = message.getId();
        this.createdAt = message.getCreatedAt();
        this.content = message.getContent();
        this.imageUrls = message.getImageUrls().size() + "";
        this.fromUserId = message.getFromUserId();
        this.toUserId = message.getToUserId();
    }
}
