package com.tdtu.user_services.controller;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.dto.request.*;
import com.tdtu.user_services.service.IFriendRequestService;
import com.tdtu.user_services.service.IUserFavouriteService;
import com.tdtu.user_services.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Service", description = "API For Upload File")
public class UserController {

    private final IUserService userService;

    private final IFriendRequestService friendRequestService;
    private final IUserFavouriteService userFavouriteService;
    @GetMapping("")
    public ResponseEntity<?> findAll(){
        ResDTO<?> response = userService.findAll();
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserInfo(@RequestHeader(name = "Authorization") String token){
        ResDTO<?> response = userService.findByToken(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{email}/for-auth")
    public ResponseEntity<?> findByEmail(@PathVariable("email") String email){
        ResDTO<?> response = userService.findByEmailResp(email);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String id){
        ResDTO<?> response = userService.findResById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @GetMapping("/get_username/{username}")
    public ResponseEntity<?> findByUserName(@PathVariable("username") String username){
        ResDTO<?> response = userService.findByUserName(username);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/by-ids")
    public ResponseEntity<?> findByIds(@RequestBody FindByIdsReqDTO request){
        ResDTO<?> response = userService.findResByIds(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody SaveUserReqDTO request){
        ResDTO<?> response = userService.saveUser(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/exists/{id}")
    public ResponseEntity<?> exists(@PathVariable("id") String id){
        ResDTO<?> response = userService.existsById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("key") String key){
        ResDTO<?> response = userService.searchByName(key);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/bio/update")
    public ResponseEntity<?> updateBio(@RequestHeader("Authorization") String token, @RequestBody UpdateBioReqDTO request){
        ResDTO<?> response = userService.updateBio(token, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/gender/update")
    public ResponseEntity<?> updateBio(@RequestHeader("Authorization") String token, @RequestBody UpdateGenderReqDTO request){
        ResDTO<?> response = userService.updateGender(token, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/name/update")
    public ResponseEntity<?> updateUserName(@RequestHeader("Authorization") String token, @RequestBody RenameReqDTO request){
        ResDTO<?> response = userService.renameUser(token, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/profile/update")
    public ResponseEntity<?> updateProfilePic(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file){
        ResDTO<?> response = userService.updatePicture(token, file, true);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/cover/update")
    public ResponseEntity<?> updateCover(@RequestHeader("Authorization") String token, @RequestParam("file") MultipartFile file){
        ResDTO<?> response = userService.updatePicture(token, file, false);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/disable")
    public ResponseEntity<?> disable(@RequestBody DisableAccountReqDTO request){
        ResDTO<?> response = userService.disableAccount(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/friend-req")
    public ResponseEntity<?> handle(@RequestBody FriendReqDTO request, @RequestHeader(name = "Authorization") String token){
        ResDTO<?> response = friendRequestService.handleFriendRequest(token, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/friend-req/acceptation")
    public ResponseEntity<?> acceptation(@RequestBody FQAcceptationDTO request){
        ResDTO<?> response = friendRequestService.friendRequestAcceptation(request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/friend-reqs")
    public ResponseEntity<?> getFriendRequests(@RequestHeader(name = "Authorization") String token){
        ResDTO<?> response = friendRequestService.getListFriendRequestResp(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/friends")
    public ResponseEntity<?> getFriends(@RequestHeader(name = "Authorization") String token){
        ResDTO<?> response = friendRequestService.getListFriendsResp(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/favourite")
    public ResponseEntity<?> saveUserFavourite(@RequestHeader(name = "Authorization") String token,
                                               @RequestBody SaveUserFavouriteDTO request){
        ResDTO<?> response = userFavouriteService.saveUserFavorite(token, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/favourite/delete/{id}")
    public ResponseEntity<?> deleteUserFavourite(@PathVariable("id") String id){
        ResDTO<?> response = userFavouriteService.deleteUserFavourite(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/registration/save")
    public ResponseEntity<?> saveRegistrationId(@RequestHeader(name = "Authorization") String token,
                                                @RequestBody SaveUserResIdReq request){
        ResDTO<?> response = userService.saveUserRegistrationId(token, request);
        log.info("Registration ID: " + request.getRegistrationId());
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
