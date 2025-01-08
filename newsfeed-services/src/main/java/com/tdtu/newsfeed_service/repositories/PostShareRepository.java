package com.tdtu.newsfeed_service.repositories;



import com.tdtu.newsfeed_service.models.PostShare;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostShareRepository extends MongoRepository<PostShare, String> {
    List<PostShare> findBySharedPostId(String sharedPost);
    List<PostShare> findBySharedUserId(String sharedUserId);
}