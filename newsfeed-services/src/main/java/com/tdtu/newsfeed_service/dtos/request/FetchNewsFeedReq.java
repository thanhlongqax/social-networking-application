package com.tdtu.newsfeed_service.dtos.request;



import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tdtu.newsfeed_service.utils.CustomLocalDateTimeDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FetchNewsFeedReq {
    @JsonDeserialize(using = CustomLocalDateTimeDeserialize.class)
    LocalDateTime startTime;
    int page;
    int size;
}