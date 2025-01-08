package com.tdtu.newsfeed_service.repositories;



import com.tdtu.newsfeed_service.models.PostShare;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomPostShareRepository {
    public List<PostShare> findSharedPostIdsByFriends(List<String> friendIds, String userId, LocalDateTime startTime);
}
