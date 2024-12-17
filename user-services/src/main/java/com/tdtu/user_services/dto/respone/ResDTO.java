package com.tdtu.user_services.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResDTO<D> {
    private int status;
    private String message;
    private D data;

}
