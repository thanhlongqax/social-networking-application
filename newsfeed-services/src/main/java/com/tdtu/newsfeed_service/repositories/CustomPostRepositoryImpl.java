package com.tdtu.newsfeed_service.repositories;



import com.tdtu.newsfeed_service.enums.EPrivacy;
import com.tdtu.newsfeed_service.models.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class CustomPostRepositoryImpl implements CustomPostRepository{
    private final MongoTemplate mongoTemplate;
    @Override
    public List<Post> findNewsFeed(String userId, List<String> friendIds, LocalDateTime startTime) {
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("userId").in(friendIds).and("privacy").in(EPrivacy.PUBLIC, EPrivacy.ONLY_FRIENDS).and("createdAt").lte(startTime),
                Criteria.where("userId").is(userId).and("createdAt").lte(startTime)
        );
        Query query = Query.query(criteria);
        return mongoTemplate.find(query, Post.class);
    }
}