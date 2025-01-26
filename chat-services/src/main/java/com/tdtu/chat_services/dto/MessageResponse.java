package com.tdtu.chat_services.dto;

import com.tdtu.chat_services.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse extends Message {
    private boolean sentByYou;
}
