package com.tdtu.friend_recommendation_services.service.Impl;

import com.tdtu.friend_recommendation_services.dto.ResDTO;
import com.tdtu.friend_recommendation_services.model.User;
import com.tdtu.friend_recommendation_services.model.UserInteraction;
import com.tdtu.friend_recommendation_services.repository.UserInteractionRepository;
import com.tdtu.friend_recommendation_services.service.IUserInteractionService;
import com.tdtu.friend_recommendation_services.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInteractionService implements IUserInteractionService {
    private final UserInteractionRepository userInteractionRepository;

    private IUserService userService;

    private UserInteraction getOrCreateInteraction(User user, User targetUser) {
        List<UserInteraction> interactions = userInteractionRepository.findByUserAndTargetUser(user, targetUser);

        if (!interactions.isEmpty()) {
            return interactions.get(0);
        }
        UserInteraction interaction = new UserInteraction();
        interaction.setUser(user);
        interaction.setTargetUser(targetUser);
        interaction.setLikes(0);
        interaction.setComments(0);
        interaction.setFollows(0);
        interaction.setMessages(0);

        return userInteractionRepository.save(interaction);
    }


    public ResDTO<?> saveLike(String userId, String targetUserId) {
        User user = userService.findById(userId);

        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        interaction.setLikes(interaction.getLikes() + 1);
        userInteractionRepository.save(interaction);

        return new ResDTO<>(200, "Like saved successfully", null);
    }
    public ResDTO<?> removeLike(String userId, String targetUserId) {
        User user = userService.findById(userId);
        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        if (interaction.getLikes() > 0) {
            interaction.setLikes(interaction.getLikes() - 1);
            userInteractionRepository.save(interaction);
        }

        return new ResDTO<>(200, "Like removed successfully", null);
    }
    public ResDTO<?> saveComment(String userId, String targetUserId) {

        User user = userService.findById(userId);

        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        interaction.setComments(interaction.getComments() + 1);
        userInteractionRepository.save(interaction);

        return new ResDTO<>(200, "Comment saved successfully", null);
    }
    public ResDTO<?> removeComment(String userId, String targetUserId) {
        User user = userService.findById(userId);
        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        if (interaction.getComments() > 0) {
            interaction.setComments(interaction.getComments() - 1);
            userInteractionRepository.save(interaction);
        }

        return new ResDTO<>(200, "Comment removed successfully", null);
    }
    public ResDTO<?> saveFollow(String userId, String targetUserId) {
        User user = userService.findById(userId);

        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        interaction.setFollows(interaction.getFollows() + 1);
        userInteractionRepository.save(interaction);

        return new ResDTO<>(200, "Follow saved successfully", null);
    }
    public ResDTO<?> removeFollow(String userId, String targetUserId) {
        User user = userService.findById(userId);
        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        if (interaction.getFollows() > 0) {
            interaction.setFollows(interaction.getFollows() - 1);
            userInteractionRepository.save(interaction);
        }

        return new ResDTO<>(200, "Unfollowed successfully", null);
    }
    public ResDTO<?> saveMessage(String userId, String targetUserId) {

        User user = userService.findById(userId);
        User targetUser = userService.findById(targetUserId);

        UserInteraction interaction = getOrCreateInteraction(user, targetUser);
        interaction.setMessages(interaction.getMessages() + 1);
        userInteractionRepository.save(interaction);

        return new ResDTO<>(200, "Message saved successfully", null);
    }
}
