package com.tdtu.user_services.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserFavourite {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;

    @ManyToOne()
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    private String name;

    @ElementCollection
    private List<String> postIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


}
