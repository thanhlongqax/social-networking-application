package com.tdtu.friend_recommendation_services.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindByIdsReqDTO {
    private List<String> userIds;
}