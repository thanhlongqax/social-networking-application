package com.tdtu.friend_recommendation_services.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_interactions")
public class UserInteraction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;

    private int likes;
    private int comments;
    private int follows;
    private int messages;

    // Tính điểm sở thích (interest score) dựa trên tương tác
    public double calculateInteractionScore() {
        return (likes * 2) + (comments * 3) + (follows * 5) + (messages * 10);
    }
}
