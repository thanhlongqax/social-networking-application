package com.tdtu.friend_recommendation_services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResDTO<D> {
    private int code;
    private String message;
    private D data;

}
