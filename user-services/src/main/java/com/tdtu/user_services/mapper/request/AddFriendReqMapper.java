package com.tdtu.user_services.mapper.request;

import com.tdtu.user_services.dto.request.FriendReqDTO;
import com.tdtu.user_services.enums.EFriendReqStatus;
import com.tdtu.user_services.model.FriendRequest;
import com.tdtu.user_services.service.Impl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AddFriendReqMapper {
    private final UserService userService;
    public FriendRequest mapToObject(String fromUserId, FriendReqDTO dto){
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setActive(true);
        friendRequest.setCreatedAt(LocalDateTime.now());
        friendRequest.setUpdatedAt(LocalDateTime.now());
        friendRequest.setStatus(EFriendReqStatus.PENDING);
        friendRequest.setFromUser(userService.findById(fromUserId));
        friendRequest.setToUser(userService.findById(dto.getToUserId()));

        return friendRequest;
    }
}
