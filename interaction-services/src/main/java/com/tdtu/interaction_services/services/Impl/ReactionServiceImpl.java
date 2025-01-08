package com.tdtu.interaction_services.services.Impl;


import com.tdtu.interaction_services.dtos.ResDTO;
import com.tdtu.interaction_services.dtos.request.DoReactRequest;
import com.tdtu.interaction_services.dtos.respone.InteractNotification;
import com.tdtu.interaction_services.dtos.respone.ReactResponse;
import com.tdtu.interaction_services.enums.EReactionType;
import com.tdtu.interaction_services.mapper.DoReactMapper;
import com.tdtu.interaction_services.mapper.ReactResponseMapper;
import com.tdtu.interaction_services.models.Post;
import com.tdtu.interaction_services.models.Reactions;
import com.tdtu.interaction_services.models.User;
import com.tdtu.interaction_services.repositories.ReactionRepository;
import com.tdtu.interaction_services.producer.SendKafkaMsgService;
import com.tdtu.interaction_services.services.PostService;
import com.tdtu.interaction_services.services.ReactionService;
import com.tdtu.interaction_services.services.UserService;
import com.tdtu.interaction_services.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionServiceImpl implements ReactionService {
    private final ReactionRepository reactionRepository;
    private final DoReactMapper reactMapper;
    private final ReactResponseMapper reactResponseMapper;
    private final UserService userServiceImpl;
    private final JwtUtils jwtUtils;
    private final SendKafkaMsgService kafkaMsgService;
    private final PostService postServiceImpl;

    public ResDTO<?> doReaction(String token, DoReactRequest request){
        ResDTO<Map<String, String>> response = new ResDTO<>();
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        Post foundPost = postServiceImpl.findById(token, request.getPostId());

        if(foundPost != null){
            AtomicReference<Boolean> isCreateNew = new AtomicReference<>();
            isCreateNew.set(false);

            Reactions reaction = reactMapper.mapToObject(userId, request);

            reactionRepository.findByUserIdAndPostId(reaction.getUserId(), reaction.getPostId())
                    .ifPresentOrElse(
                            r -> {
                                if(r.getType().equals(reaction.getType())){
                                    reactionRepository.delete(r);
                                    response.setMessage("Đã hủy tương tác");
                                }else{
                                    r.setType(reaction.getType());
                                    r.setCreatedAt(LocalDateTime.now());
                                    reactionRepository.save(r);

                                    response.setMessage("Đã cập nhật tương tác");
                                    isCreateNew.set(true);
                                }
                            }, () -> {
                                reactionRepository.save(reaction);
                                response.setMessage("Đã tương tác");
                                isCreateNew.set(true);
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

            response.setData(null);
            response.setCode(200);

            return response;
        }

        throw new RuntimeException("Post not found with id: " + request.getPostId());
    }

    public ResDTO<?> getReactsByPostId(String token, String postId){
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
