package com.tdtu.interaction_services.repositories;

import com.tdtu.interaction_services.models.PostReactionCountMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostReactionCountMongoRepository extends MongoRepository<PostReactionCountMongo, String> {
}
