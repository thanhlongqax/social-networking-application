package com.tdtu.follower_services.controller;

import com.tdtu.follower_services.dto.request.FollowerRequest;
import com.tdtu.follower_services.services.IFollowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/followers")
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
    public ResponseEntity<?> getFollowing(@RequestHeader("Authorization") String token ){
        return new ResponseEntity<>(followerService.getFollowingCount(token), HttpStatus.OK);
    }
    @GetMapping("/get_follower")
    public ResponseEntity<?> getFollower(@RequestHeader("Authorization") String token ){
        return new ResponseEntity<>(followerService.getFollowerCount(token), HttpStatus.OK);
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
