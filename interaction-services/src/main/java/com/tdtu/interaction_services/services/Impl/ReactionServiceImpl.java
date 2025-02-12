package com.tdtu.interaction_services.services.Impl;


import com.tdtu.interaction_services.dtos.ResDTO;
import com.tdtu.interaction_services.dtos.request.DoReactRequest;
import com.tdtu.interaction_services.dtos.respone.InteractNotification;
import com.tdtu.interaction_services.dtos.respone.ReactResponse;
import com.tdtu.interaction_services.dtos.respone.ReactResponseNumOfReaction;
import com.tdtu.interaction_services.dtos.respone.isReactionResponse;
import com.tdtu.interaction_services.enums.EReactionType;
import com.tdtu.interaction_services.mapper.DoReactMapper;
import com.tdtu.interaction_services.mapper.ReactResponseMapper;
import com.tdtu.interaction_services.models.Post;
import com.tdtu.interaction_services.models.PostReactionCountMongo;
import com.tdtu.interaction_services.models.Reactions;
import com.tdtu.interaction_services.models.User;
import com.tdtu.interaction_services.repositories.PostReactionCountMongoRepository;
import com.tdtu.interaction_services.repositories.ReactionRepository;
import com.tdtu.interaction_services.producer.SendKafkaMsgService;
import com.tdtu.interaction_services.services.PostService;
import com.tdtu.interaction_services.services.ReactionService;
import com.tdtu.interaction_services.services.UserService;
import com.tdtu.interaction_services.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final DoReactMapper reactMapper;
    private final ReactResponseMapper reactResponseMapper;
    private final UserService userServiceImpl;
    private final JwtUtils jwtUtils;
    private final SendKafkaMsgService kafkaMsgService;
    private final PostService postServiceImpl;
    private final RedisService redisService;
    private final PostReactionCountMongoRepository postReactionCountMongo;
    private static final String REACT_CACHE_PREFIX = "reactions::";
    public ResDTO<?> doReaction(String token, DoReactRequest request){
        ResDTO<isReactionResponse> response = new ResDTO<>();
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        Post foundPost = postServiceImpl.findById(token, request.getPostId());
        if(foundPost != null){
            AtomicReference<Boolean> isCreateNew = new AtomicReference<>();
            isCreateNew.set(false);

            Reactions reaction = reactMapper.mapToObject(userId, request);
            isReactionResponse isReaction = new isReactionResponse();
            log.info("lấy type",reaction);
            reactionRepository.findByUserIdAndPostId(reaction.getUserId(), reaction.getPostId())
                    .ifPresentOrElse(
                            r -> {
                                if(r.getType().equals(reaction.getType())){
                                    // nếu reaction giống nhau thì xóa reactions
                                    reactionRepository.delete(r);
                                    isReaction.setReactions(false);
                                    response.setData(isReaction);
                                    response.setMessage("Đã hủy tương tác");

                                    updateReactionCount(reaction.getPostId(), reaction.getType(), 0);
                                }else{
                                    // nếu reaction khác nhau thì cập nhật
                                    r.setType(reaction.getType());
                                    r.setCreatedAt(LocalDateTime.now());
                                    reactionRepository.save(r);
                                    isReaction.setReactions(true);
                                    response.setData(isReaction);
                                    response.setMessage("Đã cập nhật tương tác");
                                    isCreateNew.set(true);


                                    updateReactionCount(reaction.getPostId(), reaction.getType(), 1);
                                }
                            }, () -> {
                                // nếu chưa có thì tạo mới reaction
                                reactionRepository.save(reaction);
                                isReaction.setReactions(true);
                                response.setMessage("Đã tương tác");
                                isCreateNew.set(true);

                                updateReactionCount(reaction.getPostId(), reaction.getType(), 1);
                            }
                    );

            if(isCreateNew.get()){
                User foundUser = userServiceImpl.findById(userId);
                if(foundUser != null && !foundUser.getId().equals(foundPost.getUser().getId())){
                    InteractNotification notification = new InteractNotification();
                    notification.setUserFullName(String.join(" ", foundUser.getFirstName(), foundUser.getMiddleName(), foundUser.getLastName()));
                    notification.setAvatarUrl(foundUser.getProfilePicture());
                    notification.setContent(notification.getUserFullName() + " đã bày tỏ cảm xúc về bài viết của bạn.");
                    notification.setPostId(reaction.getPostId());
                    notification.setTitle("Có người tương tác nè!");

                    kafkaMsgService.publishInteractNoti(notification);
                }
            }

            response.setData(isReaction);
            response.setCode(200);

            return response;
        }

        throw new RuntimeException("Post not found with id: " + request.getPostId());
    }
    private void updateReactionCount(String postId, EReactionType reactionType, int delta) {
        PostReactionCountMongo reactionCount = postReactionCountMongo.findById(postId)
                .orElse(new PostReactionCountMongo(postId, 0, 0, 0, 0, 0, 0));

        switch (reactionType) {
            case LIKE -> reactionCount.setLikeCount(reactionCount.getLikeCount() + delta);
            case HEART -> reactionCount.setLoveCount(reactionCount.getLoveCount() + delta);
            case HAHA -> reactionCount.setHahaCount(reactionCount.getHahaCount() + delta);
            case WOW -> reactionCount.setWowCount(reactionCount.getWowCount() + delta);
            case SAD -> reactionCount.setSadCount(reactionCount.getSadCount() + delta);
            case ANGRY -> reactionCount.setAngryCount(reactionCount.getAngryCount() + delta);
        }

        postReactionCountMongo.save(reactionCount);
    }
    public ResDTO<?> getReactsByPostId(String token, String postId){
        ResDTO<ReactResponseNumOfReaction> response = new ResDTO<>();
        String userId;

        if(token != null && !token.isEmpty())
            userId = jwtUtils.getUserIdFromJwtToken(token);
        else {
            userId = "";
        }

        List<Reactions> reactions = reactionRepository.findReactionsByPostIdOrderByCreatedAtDesc(postId);
        List<String> userIds = reactions.stream().map(Reactions::getUserId).toList();
        List<User> users = userServiceImpl.findByIds(userIds);

        Map<EReactionType, List<ReactResponse>> reactResponses = reactionRepository.findReactionsByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(r -> reactResponseMapper.mapToDto(userId, r, users))
                .collect(Collectors.groupingBy(ReactResponse::getType));
        int totalElements = reactResponses != null ? reactResponses.values().stream()
                .mapToInt(List::size)
                .sum() : 0;
        // Kiểm tra người dùng đã react chưa
        boolean hasReacted = reactResponses.values().stream()
                .flatMap(List::stream) // Flatten danh sách
                .anyMatch(reaction -> reaction.getUser().getId().equals(userId)); // Kiểm tra userId

        ReactResponseNumOfReaction result = new ReactResponseNumOfReaction();
        result.setReactions(reactResponses);
        result.setCountReactions(totalElements);
        result.setHasReacted(hasReacted);
        response.setCode(200);
        response.setData(result);
        response.setMessage("success");

        return response;
    }
    public ResDTO<?> getReactsByPostIdForPostService(String token, String postId){
        ResDTO<Map<EReactionType, List<ReactResponse>>> response = new ResDTO<>();
        String userId;

        if(token != null && !token.isEmpty())
            userId = jwtUtils.getUserIdFromJwtToken(token);
        else {
            userId = "";
        }

        List<Reactions> reactions = reactionRepository.findReactionsByPostIdOrderByCreatedAtDesc(postId);
        List<String> userIds = reactions.stream().map(Reactions::getUserId).toList();
        List<User> users = userServiceImpl.findByIds(userIds);

        Map<EReactionType, List<ReactResponse>> reactResponses = reactionRepository.findReactionsByPostIdOrderByCreatedAtDesc(postId)
                .stream()
                .map(r -> reactResponseMapper.mapToDto(userId, r, users))
                .collect(Collectors.groupingBy(ReactResponse::getType));

        response.setCode(200);
        response.setData(reactResponses);
        response.setMessage("success");

        return response;
    }

}
