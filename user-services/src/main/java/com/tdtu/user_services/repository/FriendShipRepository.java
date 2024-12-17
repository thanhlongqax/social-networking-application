package com.tdtu.user_services.repository;

import com.tdtu.user_services.enums.EFriendshipStatus;
import com.tdtu.user_services.model.Friendship;
import com.tdtu.user_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendShipRepository extends JpaRepository<Friendship , String> {
    List<Friendship> findByToUserAndActiveAndStatus(User toUser, boolean active, EFriendshipStatus status);
    List<Friendship> findByFromUserAndStatusOrToUserAndStatus(User fromUser, EFriendshipStatus status1, User toUser, EFriendshipStatus status2);
    List<Friendship> findByToUserAndFromUserOrFromUserAndToUser(User toUser, User fromUser, User fromUser2, User toUser2);
}
