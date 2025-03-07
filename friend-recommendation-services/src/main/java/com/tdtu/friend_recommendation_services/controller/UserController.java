package com.tdtu.friend_recommendation_services.controller;

import com.tdtu.friend_recommendation_services.service.IUserFavouriteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Service", description = "API For User")
public class UserController {

    private final IUserService userService;

    private final IFriendRequestService friendRequestService;
    private final IUserFavouriteService userFavouriteService;

}
