package com.tdtu.newsfeed_service.dtos.respone;



import com.fasterxml.jackson.annotation.JsonFormat;
import com.tdtu.newsfeed_service.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareInfo implements Serializable {
    private String id;
    private String status;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date sharedAt;
    private User sharedUser;
}