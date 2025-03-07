package com.tdtu.user_services.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToElasticSearchDTO {

    private String id;

    private String username;

    private String firstName;

    private String middleName;

    private String lastName;

    private String userFullName;

}
