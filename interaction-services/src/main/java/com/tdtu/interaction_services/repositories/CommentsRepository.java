package com.tdtu.interaction_services.repositories;


import com.tdtu.interaction_services.models.Comments;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CommentsRepository extends MongoRepository<Comments, String> {
    List<Comments> findByPostIdAndParentIdIsNull(String postId);
    List<Comments> findByParentId(String parentId);
}