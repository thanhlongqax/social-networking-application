package com.tdtu.user_services.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FQAcceptationDTO {
    private String friendReqId;
    private Boolean isAccept;
}