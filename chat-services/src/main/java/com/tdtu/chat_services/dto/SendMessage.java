package com.tdtu.chat_services.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendMessage {
    private String content;
    private List<String> imageUrls;
    private String fromUserId;
    private String toUserId;
//    private String roomId;
}
