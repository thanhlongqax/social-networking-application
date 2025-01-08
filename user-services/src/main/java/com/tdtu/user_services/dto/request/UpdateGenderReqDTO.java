package com.tdtu.user_services.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateGenderReqDTO {
    //    private String userId;
    private String gender;
}