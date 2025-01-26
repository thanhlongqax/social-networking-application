package com.tdtu.user_services.repository;

import com.tdtu.user_services.enums.EFriendReqStatus;
import com.tdtu.user_services.model.FriendRequest;
import com.tdtu.user_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {
    List<FriendRequest> findByToUserAndActiveAndStatus(User toUser, boolean active, EFriendReqStatus status);
    List<FriendRequest> findByFromUserAndStatusOrToUserAndStatus(User fromUser, EFriendReqStatus status1, User toUser, EFriendReqStatus status2);
    List<FriendRequest> findByToUserAndFromUserOrFromUserAndToUser(User toUser, User fromUser, User fromUser2, User toUser2);
}