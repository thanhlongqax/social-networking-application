package com.tdtu.follower_services.controller;

import com.tdtu.follower_services.dto.request.FollowerRequest;
import com.tdtu.follower_services.services.IFollowerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/followers")
@Tag(name = "Follower Service", description = "API For Follower ")
public class FollowerController {
    @Autowired
    private IFollowerService followerService;
    @PostMapping("/create")
    public ResponseEntity<?> saveFollower(@RequestHeader("Authorization") String idUser, @RequestBody FollowerRequest request){
        return new ResponseEntity<>(followerService.saveFollow(idUser,request), HttpStatus.OK);
    }

    @PostMapping("/unfollow")
    public ResponseEntity<?> unFollow(@RequestHeader("Authorization") String token,@RequestBody FollowerRequest request){
        return new ResponseEntity<>(followerService.unFollow(token,request), HttpStatus.OK);
    }
    @GetMapping("/get_following")
    public ResponseEntity<?> getFollowing(
            @RequestHeader("Authorization") String token,
            @RequestParam String id,
            @RequestParam(value = "search", required = false) String search){
        return new ResponseEntity<>(followerService.getFollowingCount(token ,id,search), HttpStatus.OK);
    }
    @GetMapping("/get_follower")
    public ResponseEntity<?> getFollower(@RequestHeader("Authorization") String token ,
                                         @RequestParam String id,
                                         @RequestParam(value = "search", required = false) String search){
        return new ResponseEntity<>(followerService.getFollowerCount(token ,id,search), HttpStatus.OK);
    }
    @GetMapping("/get_followed_ids")
    public ResponseEntity<?> getFollowerIds(@RequestHeader("Authorization") String token){
        return new ResponseEntity<>(followerService.findFollowedUserIdsByToken(token), HttpStatus.OK);
    }
    @GetMapping("/status")
    public ResponseEntity<?> isFollowing(
            @RequestHeader("Authorization") String token,
            @RequestParam String targetUserId) {
        return new ResponseEntity<>(followerService.isFollowing(token, targetUserId), HttpStatus.OK);
    }
//    @GetMapping("/{userId}/count")
//    public ResponseEntity<Map<String, Long>> getFollowerAndFollowingCount(@PathVariable String userId) {
//        Long followerCount = followerService.getFollowerCount(userId);
//        Long followingCount = followerService.getFollowingCount(userId);
//
//        Map<String, Long> response = new HashMap<>();
//        response.put("followers", followerCount);
//        response.put("following", followingCount);
//
//        return ResponseEntity.ok(response);
//    }

}
