package com.tdtu.search_services.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String username;

    @Field(type = FieldType.Text)
    private String firstName;

    @Field(type = FieldType.Text)
    private String middleName;

    @Field(type = FieldType.Text)
    private String lastName;

    @Field(type = FieldType.Text)
    private String userFullName;

    public String getUserFullName() {
        return (firstName != null ? firstName + " " : "") +
                (middleName != null ? middleName + " " : "") +
                (lastName != null ? lastName : "");
    }
}
