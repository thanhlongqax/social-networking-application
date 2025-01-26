package com.tdtu.follower_services.services.Impl;

import com.tdtu.follower_services.dto.ResDTO;
import com.tdtu.follower_services.dto.request.FollowerRequest;
import com.tdtu.follower_services.dto.respone.FollowerCountResponse;
import com.tdtu.follower_services.dto.respone.FollowerNotification;
import com.tdtu.follower_services.dto.respone.FollowerResponse;
import com.tdtu.follower_services.models.Follower;
import com.tdtu.follower_services.producer.FollowerEventProducer;
import com.tdtu.follower_services.producer.SendKafkaMsgService;
import com.tdtu.follower_services.repository.FollowerRepository;
import com.tdtu.follower_services.services.IFollowerService;
import com.tdtu.follower_services.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FollowerService implements IFollowerService {
    @Autowired
    private FollowerRepository followerRepository;

    @Autowired
    private SendKafkaMsgService sendKafkaMsgService;

    @Autowired
    private FollowerEventProducer followerEventProducer;

    @Autowired
    private JwtUtils jwtUtils;
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowerService.class);

    public ResDTO<?> saveFollow(String token, FollowerRequest request) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        request.setFollowingId(userId);
        ResDTO<?> respone = new ResDTO<>();
        if(request.getFollowerId()!=null && request.getFollowingId()!=null && !request.getFollowerId().equals(request.getFollowingId())){
            Follower isFollowExist = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingTrue(request.getFollowerId(), request.getFollowingId());

            if(isFollowExist!=null){
                respone.setCode(HttpStatus.BAD_REQUEST.hashCode());
                respone.setMessage("Follow exist");
                respone.setData(null);
            }else{
                Follower isFollowExistAndUnfollow = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingFalse(request.getFollowerId(), request.getFollowingId());
                if(isFollowExistAndUnfollow!=null){
                    isFollowExistAndUnfollow.setIsFollowing(true);
                    followerRepository.save(isFollowExistAndUnfollow);

                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.setFollowerId(isFollowExistAndUnfollow.getFollowerId());
                    followerResponse.setFollowingId(isFollowExistAndUnfollow.getFollowingId());
                    followerResponse.setIsFollowing(isFollowExistAndUnfollow.getIsFollowing());

                    followerEventProducer.sendFollowEvent(followerResponse);

                    FollowerNotification followerNotification = new FollowerNotification();
                    followerNotification.setUserId(isFollowExistAndUnfollow.getFollowerId());
                    followerNotification.setContent("Có người follower nè");
                    followerNotification.setTimestamp(new Date());
                    followerNotification.setTitle("FOLLOWER");

                    followerNotification.setRead(false);
                    sendKafkaMsgService.publishInteractNoti(followerNotification);

                }else{
                    Follower follower = new Follower();
                    follower.setFollowerId(request.getFollowerId());
                    follower.setFollowingId(request.getFollowingId());
                    followerRepository.save(follower);
                    FollowerResponse followerResponse = new FollowerResponse();
                    followerResponse.setFollowerId(follower.getFollowerId());
                    followerResponse.setFollowingId(follower.getFollowingId());
                    followerResponse.setIsFollowing(follower.getIsFollowing());

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

    public ResDTO<?> unFollow(String idUser,FollowerRequest request) {
        ResDTO<?> baseResponse = new ResDTO<>();
        if(request.getFollowerId()!=null && request.getFollowingId()!=null && !request.getFollowerId().equals(request.getFollowingId())){
            Follower isFollowExist = followerRepository.findByFollowerIdAndFollowingIdAndIsFollowingTrue(request.getFollowerId(), request.getFollowingId());

            if(isFollowExist==null){
                baseResponse.setCode(HttpStatus.BAD_REQUEST.hashCode());
                baseResponse.setMessage("Relation not exist");
            }else{
                isFollowExist.setIsFollowing(false);
                followerRepository.save(isFollowExist);
                FollowerResponse followerResponse = new FollowerResponse();
                followerResponse.setIsFollowing(isFollowExist.getIsFollowing());
                followerResponse.setFollowerId(isFollowExist.getFollowerId());
                followerResponse.setFollowingId(isFollowExist.getFollowingId());

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
    public ResDTO<?> getFollowerCount(String token) {
        String userId  = jwtUtils.getUserIdFromJwtToken(token);
        Long countFollowers =  followerRepository.countFollowers(userId);
        ResDTO<FollowerCountResponse> baseRespone = new ResDTO<>();
        baseRespone.setCode(HttpStatus.OK.value());
        baseRespone.setMessage("Lấy số lượng followers thành công");
        FollowerCountResponse respone = new FollowerCountResponse();
        respone.setCountFollower(countFollowers);
        baseRespone.setData(respone);

        return baseRespone;
    }

    public ResDTO<?> getFollowingCount(String token) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        Long countFollowing =  followerRepository.countFollowing(userId);
        ResDTO<FollowerCountResponse> baseRespone = new ResDTO<>();
        baseRespone.setCode(HttpStatus.OK.value());
        baseRespone.setMessage("Lấy số lượng followers thành công");
        FollowerCountResponse respone = new FollowerCountResponse();
        respone.setCountFollowing(countFollowing);
        baseRespone.setData(respone);
        return baseRespone;
    }


}
