package com.tdtu.newsfeed_service.mapper.respone;



import com.tdtu.newsfeed_service.dtos.respone.PostResponse;
import com.tdtu.newsfeed_service.dtos.respone.TopReacts;
import com.tdtu.newsfeed_service.enums.EReactionType;
import com.tdtu.newsfeed_service.models.*;
import com.tdtu.newsfeed_service.repositories.PostShareRepository;
import com.tdtu.newsfeed_service.services.InteractionService;
import com.tdtu.newsfeed_service.services.UserService;
import com.tdtu.newsfeed_service.utils.DateUtils;
import com.tdtu.newsfeed_service.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostResponseMapper {
    private final UserService userService;
    private final InteractionService interactionService;
    private final PostShareRepository postShareRepository;
    private final JwtUtils jwtUtils;

    public PostResponse mapToDto(String token, Post post){
        User postedUser = userService.findById(post.getUserId());
        List<User> taggedUsers = post.getPostTags().stream().map(PostTag::getTaggedUser).toList();

        PostResponse postResponse = new PostResponse();

        postResponse.setId(post.getId());
        postResponse.setPrivacy(post.getPrivacy());
        postResponse.setContent(post.getContent());
        postResponse.setType(post.getType());
        postResponse.setImageUrls(post.getImageUrls());
        postResponse.setVideoUrls(post.getVideoUrls());
        postResponse.setCreatedAt(DateUtils.localDateTimeToDate(post.getCreatedAt() != null ? post.getCreatedAt() : LocalDateTime.now()));
        postResponse.setUpdatedAt(DateUtils.localDateTimeToDate(post.getCreatedAt() != null ? post.getCreatedAt() : LocalDateTime.now()));
        postResponse.setNoShared(postShareRepository.findBySharedPostId(post.getId()).size());
        postResponse.setUser(postedUser);
        postResponse.setTaggedUsers(taggedUsers);

        Map<EReactionType, List<Reacts>> reactionsMap = interactionService.findReactionsByPostId(token, post.getId());

        int totalElements = reactionsMap != null ? reactionsMap.values().stream()
                .mapToInt(List::size)
                .sum() : 0;

        List<Comment> comments = interactionService.findCommentsByPostId(token, post.getId());

        postResponse.setTopReacts(findTopReact(reactionsMap != null ? reactionsMap : new HashMap<>()));
        postResponse.setNoReactions(totalElements);
        postResponse.setNoComments(comments != null ? comments.size() : 0);

        String userId;
        if(token != null && !token.isEmpty()){
            userId = jwtUtils.getUserIdFromJwtToken(token);
        }else{
            userId = "";
        }

        if(reactionsMap != null){
            Reacts react = findUserReaction(reactionsMap, userId);
            postResponse.setReacted(react != null ? react.getType() : null);
        }

        return postResponse;
    }

    public static Reacts findUserReaction(Map<EReactionType, List<Reacts>> reactionsMap, String userId) {
        for (Map.Entry<EReactionType, List<Reacts>> entry : reactionsMap.entrySet()) {
            List<Reacts> reactsList = entry.getValue();

            for (Reacts react : reactsList) {
                if (react.getUser().getId().equals(userId)) {
                    return react;
                }
            }
        }
        return null;
    }

    private List<TopReacts> findTopReact(Map<EReactionType, List<Reacts>> reactionsMap){
        Map<EReactionType, List<Reacts>> sortedMap = reactionsMap.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .limit(3)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        List<TopReacts> topReacts = new ArrayList<>();

        sortedMap.forEach((k, v) -> {
            TopReacts react = new TopReacts();
            react.setCount(v.size());
            react.setType(k);

            topReacts.add(react);
        });

        return topReacts;
    }
}
