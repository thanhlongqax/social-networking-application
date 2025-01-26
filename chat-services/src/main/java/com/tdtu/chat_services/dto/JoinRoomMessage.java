package com.tdtu.chat_services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinRoomMessage {
    private String fromUserId;
    private String toUserId;
}