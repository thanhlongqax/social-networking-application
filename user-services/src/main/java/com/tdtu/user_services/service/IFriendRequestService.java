package com.tdtu.user_services.service;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.dto.request.FQAcceptationDTO;
import com.tdtu.user_services.dto.request.FriendReqDTO;
import com.tdtu.user_services.model.FriendRequest;
import com.tdtu.user_services.model.User;

import java.util.List;

public interface IFriendRequestService {
    public ResDTO<?> handleFriendRequest(String token, FriendReqDTO request);
    public List<User> getListFriends(String userId);
    public ResDTO<?> getListFriendsResp(String token);
    public List<FriendRequest> getListFriendRequest(String userId);
    public ResDTO<?> getListFriendRequestResp(String token);
    public List<User> getFromUsersViaRequests(List<FriendRequest> friendRequests);
    public ResDTO<?> friendRequestAcceptation(FQAcceptationDTO request);


}
