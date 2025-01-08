package com.tdtu.user_services.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FCMRegistrationIdsBody {
    private String operation;
    private String notification_key_name;
    private List<String> registration_ids;
    private String notification_key;
}