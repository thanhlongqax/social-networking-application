package com.tdtu.interaction_services.repositories;


import com.tdtu.interaction_services.models.Reactions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends MongoRepository<Reactions, String> {
    Optional<Reactions> findByUserIdAndPostId(String userId, String postId);
    List<Reactions> findReactionsByPostIdOrderByCreatedAtDesc(String postId);
}