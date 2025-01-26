package com.tdtu.chat_services.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.chat_services.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private String id;
    private String userId1;
    private String userId2;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    private List<MessageResponse> messages;
    private Message latestMessage;
    private String roomName;
    private String roomImage;
    private String opponentUserId;
}
