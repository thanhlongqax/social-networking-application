package com.tdtu.follower_services.services.Impl;

import com.tdtu.follower_services.dto.ResDTO;
import com.tdtu.follower_services.dto.request.FollowerRequest;
import com.tdtu.follower_services.dto.respone.FollowerCountResponse;
import com.tdtu.follower_services.dto.respone.FollowerNotification;
import com.tdtu.follower_services.dto.respone.FollowerResponse;
import com.tdtu.follower_services.dto.respone.FollowingCountResponse;
import com.tdtu.follower_services.models.Follower;
import com.tdtu.follower_services.models.User;
import com.tdtu.follower_services.producer.FollowerEventProducer;
import com.tdtu.follower_services.producer.SendKafkaMsgService;
import com.tdtu.follower_services.repository.FollowerRepository;
import com.tdtu.follower_services.services.IFollowerService;
import com.tdtu.follower_services.services.IUserService;
import com.tdtu.follower_services.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FollowerService implements IFollowerService {
    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private SendKafkaMsgService sendKafkaMsgService;

    @Autowired
    private FollowerEventProducer followerEventProducer;
    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowerService.class);

    public ResDTO<?> saveFollow(String token, FollowerRequest request) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        request.setFromTo(userId);
        ResDTO<?> respone = new ResDTO<>();
        if(request.getFromTo()!=null && request.getUserId()!=null && !request.getFromTo().equals(request.getUserId())){
            Follower isFollowExist = followerRepository.findByFollowerUserIdAndFollowingUserIdAndActiveFollowTrue(request.getFromTo(), request.getUserId());

            if(isFollowExist!=null){
                respone.setCode(HttpStatus.BAD_REQUEST.hashCode());
                respone.setMessage("Follow exist");
                respone.setData(null);
            }else{
                Follower isFollowExistAndUnfollow = followerRepository.findByFollowerUserIdAndFollowingUserIdAndActiveFollowFalse(request.getFromTo(), request.getUserId());
                if(isFollowExistAndUnfollow!=null){
                    isFollowExistAndUnfollow.setActiveFollow(true);
                    followerRepository.save(isFollowExistAndUnfollow);

                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.setFollowerUserId(isFollowExistAndUnfollow.getFollowerUserId());
                    followerResponse.setFollowingUserId(isFollowExistAndUnfollow.getFollowingUserId());
                    followerResponse.setActiveFollow(isFollowExistAndUnfollow.getActiveFollow());

                    followerEventProducer.sendFollowEvent(followerResponse);

                    FollowerNotification followerNotification = new FollowerNotification();
                    followerNotification.setUserId(isFollowExistAndUnfollow.getFollowerUserId());
                    followerNotification.setContent("Có người follower nè");
                    followerNotification.setTimestamp(new Date());
                    followerNotification.setTitle("FOLLOWER");

                    followerNotification.setRead(false);
                    sendKafkaMsgService.publishInteractNoti(followerNotification);

                }else{
                    Follower follower = new Follower();
                    follower.setFollowerUserId(request.getFromTo());
                    follower.setFollowingUserId(request.getUserId());
                    followerRepository.save(follower);

                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.setFollowingUserId(follower.getFollowingUserId());
                    followerResponse.setFollowerUserId(follower.getFollowerUserId());
                    followerResponse.setActiveFollow(follower.getActiveFollow());

                    followerEventProducer.sendFollowEvent(followerResponse);
                }
                respone.setCode(HttpStatus.OK.hashCode());
                respone.setMessage("Follow success");
                respone.setData(null);
            }
        }else{
            respone.setCode(HttpStatus.BAD_REQUEST.hashCode());
            respone.setMessage("FollowerId/ FollowingId not found or Not allow follow yourself");
        }
        return respone;
    }

    public ResDTO<?> unFollow(String token,FollowerRequest request) {
        String fromTo = jwtUtils.getUserIdFromJwtToken(token);
        request.setFromTo(fromTo);
        ResDTO<?> baseResponse = new ResDTO<>();

        if(request.getFromTo()!=null && request.getUserId()!=null && !request.getFromTo().equals(request.getUserId())){
            Follower isFollowExist = followerRepository.findByFollowerUserIdAndFollowingUserIdAndActiveFollowTrue(request.getFromTo(), request.getUserId());

            if(isFollowExist==null){
                baseResponse.setCode(HttpStatus.BAD_REQUEST.hashCode());
                baseResponse.setMessage("Relation not exist");
            }else{
                isFollowExist.setActiveFollow(false);
                followerRepository.save(isFollowExist);

                FollowerResponse followerResponse = new FollowerResponse();
                followerResponse.setFollowerUserId(isFollowExist.getFollowerUserId());
                followerResponse.setFollowingUserId(isFollowExist.getFollowingUserId());
                followerResponse.setActiveFollow(isFollowExist.getActiveFollow());

                followerEventProducer.sendUnfollowEvent(followerResponse);

                baseResponse.setCode(HttpStatus.OK.hashCode());
                baseResponse.setMessage("Unfollow thành công");


            }
        }else{
            baseResponse.setCode(HttpStatus.BAD_REQUEST.hashCode());
            baseResponse.setMessage("FollowerId/ FollowingId not found or Not un allow follow yourself");
        }
        return baseResponse;
    }
    public ResDTO<?> getFollowerCount(String token , String search) {
        String userId  = jwtUtils.getUserIdFromJwtToken(token);
        Long countFollowers =  followerRepository.countByFollowingUserIdAndActiveFollow(userId , true);

        List<User> userByFollowingId = userService.findByIds(
                followerRepository.findByFollowingUserIdAndActiveFollowTrue(userId)
                        .stream()
                        .map(Follower::getFollowingUserId)
                        .toList()
        );
        if (search != null && !search.isEmpty()) {
            String lowerSearch = search.toLowerCase();
            userByFollowingId = userByFollowingId.stream()
                    .filter(user ->
                            user.getUserFullName().toLowerCase().contains(lowerSearch) ||
                                    user.getUsername().toLowerCase().contains(lowerSearch) ||
                                    user.getFirstName().toLowerCase().contains(lowerSearch) ||
                                    user.getLastName().toLowerCase().contains(lowerSearch)
                    )
                    .toList();
        }

        FollowerCountResponse respone = new FollowerCountResponse();
        respone.setCountFollower(countFollowers);
        respone.setUser(userByFollowingId);

        ResDTO<FollowerCountResponse> baseRespone = new ResDTO<>();
        baseRespone.setCode(HttpStatus.OK.value());
        baseRespone.setMessage("Lấy số lượng người theo dõi mình thành công");
        baseRespone.setData(respone);

        return baseRespone;
    }

    public ResDTO<?> getFollowingCount(String token , String search) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        Long countFollowing =  followerRepository.countByFollowerUserIdAndActiveFollow(userId , true);

        List<User> userByFollowingId = userService.findByIds(
                followerRepository.findByFollowerUserIdAndActiveFollowTrue(userId)
                        .stream()
                        .map(Follower::getFollowingUserId)
                        .toList()
        );

        if (search != null && !search.isEmpty()) {
            String lowerSearch = search.toLowerCase();
            userByFollowingId = userByFollowingId.stream()
                    .filter(user ->
                            user.getUserFullName().toLowerCase().contains(lowerSearch) ||
                                    user.getUsername().toLowerCase().contains(lowerSearch) ||
                                    user.getFirstName().toLowerCase().contains(lowerSearch) ||
                                    user.getLastName().toLowerCase().contains(lowerSearch)
                    )
                    .toList();
        }
        FollowingCountResponse respone = new FollowingCountResponse();
        respone.setCountFollowing(countFollowing);
        respone.setUser(userByFollowingId);

        ResDTO<FollowingCountResponse> baseRespone = new ResDTO<>();
        baseRespone.setCode(HttpStatus.OK.value());
        baseRespone.setMessage("Lấy số lượng người mình đang theo dõi thành công");
        baseRespone.setData(respone);
        return baseRespone;
    }


}
