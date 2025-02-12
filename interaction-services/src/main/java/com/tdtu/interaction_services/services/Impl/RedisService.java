package com.tdtu.interaction_services.services.Impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tdtu.interaction_services.dtos.respone.ReactResponse;
import com.tdtu.interaction_services.enums.EReactionType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Object, Object> hashOperations;
    @Autowired
    private ObjectMapper objectMapper;
    private static final String REACT_CACHE_PREFIX = "reactions:";
    @PostConstruct
    public void init(){
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate is not initialized!");
        }
        this.hashOperations = redisTemplate.opsForHash();
    }

    // Lưu reaction vào Redis Hash
    public void saveReaction(String postId, String userId, EReactionType type) {
        String key = REACT_CACHE_PREFIX + postId;
        log.info("🔵 saveReaction - postId: {}, userId: {}, reactionType: {}", postId, userId, type);
        Object savedReaction = redisTemplate.opsForHash().get(key, userId);
        log.info("✅ Reaction saved successfully - Key: {}, User: {}, Value: {}", key, userId, savedReaction);
    }

    // Lưu toàn bộ danh sách reactions dưới dạng JSON (có thời gian sống)
    public void saveReactions(String postId, Object data, long timeoutSeconds) {
        String key = REACT_CACHE_PREFIX + postId;

        redisTemplate.opsForValue().set(key, data, Duration.ofSeconds(timeoutSeconds));
    }

    // Lấy toàn bộ reactions từ Redis Hash
    public Map<EReactionType, List<ReactResponse>> getReactions(String postId) {
        Map<Object, Object> cachedData = hashOperations.entries("post_reaction_count:" + postId);
        if (cachedData == null || cachedData.isEmpty()) {
            return null;
        }

        Map<EReactionType, List<ReactResponse>> reactionMap = new HashMap<>();
        cachedData.forEach((key, value) -> {
            try {
                EReactionType reactionType = EReactionType.valueOf(key.toString());
                List<ReactResponse> reactResponses = objectMapper.readValue(value.toString(), new TypeReference<>() {});
                reactionMap.put(reactionType, reactResponses);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return reactionMap;
    }

    // Kiểm tra user đã react hay chưa
    public EReactionType getUserReaction(String postId, String userId) {
        String key = REACT_CACHE_PREFIX + postId;
        Object reaction = redisTemplate.opsForHash().get(key, userId);
        return reaction != null ? EReactionType.valueOf(reaction.toString()) : null;
    }

    // Xóa reaction của user trong Redis Hash
    public void removeReaction(String postId, String userId) {
        String key = REACT_CACHE_PREFIX + postId;
        redisTemplate.opsForHash().delete(key, userId);
    }

    // Xóa toàn bộ reactions của bài viết
    public void clearReactionsCache(String postId) {
        String key = REACT_CACHE_PREFIX + postId;
        redisTemplate.delete(key);
    }
}
