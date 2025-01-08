package com.tdtu.newsfeed_service.repositories;


import com.tdtu.newsfeed_service.models.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomPostRepository {
    List<Post> findNewsFeed(String userId, List<String> friendIds, LocalDateTime startTime);
}
