package com.tdtu.user_services.service.Impl;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.dto.request.SaveUserFavouriteDTO;
import com.tdtu.user_services.model.User;
import com.tdtu.user_services.model.UserFavourite;
import com.tdtu.user_services.repository.UserFavoriteRepository;
import com.tdtu.user_services.service.IUserFavouriteService;
import com.tdtu.user_services.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserFavouriteService implements IUserFavouriteService {
    private final UserFavoriteRepository userFavouriteRepository;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public ResDTO<?> saveUserFavorite(String token, SaveUserFavouriteDTO request){
        AtomicReference<String> message = new AtomicReference<>();
        AtomicReference<String> savedId = new AtomicReference<>();
        User foundUser = userService.findById(jwtUtils.getUserIdFromJwtToken(token));

        userFavouriteRepository.findByNameAndUser(request.getName(), foundUser)
                .ifPresentOrElse(
                        (f) -> {
                            if(f.getPostIds() != null){
                                if(!f.getPostIds().contains(request.getPostId())){
                                    f.getPostIds().add(request.getPostId());
                                    message.set("Đã thêm vào danh sách yêu thích: " + request.getName());
                                }else{
                                    f.getPostIds().remove(request.getPostId());
                                    message.set("Đã xóa khỏi danh sách yêu thích: " + request.getName());
                                }
                            }else{
                                List<String> postIds = new ArrayList<>();
                                postIds.add(request.getPostId());
                                f.setPostIds(postIds);
                                message.set("Đã thêm vào danh sách yêu thích: " + request.getName());
                            }

                            f.setUpdatedAt(LocalDateTime.now());
                            savedId.set(f.getId());
                            userFavouriteRepository.save(f);
                        }, () -> {
                            UserFavourite userFavourite = new UserFavourite();
                            userFavourite.setName(request.getName());
                            userFavourite.setUser(foundUser);
                            userFavourite.setCreatedAt(LocalDateTime.now());

                            List<String> postIds = new ArrayList<>();
                            postIds.add(request.getPostId());

                            userFavourite.setPostIds(postIds);
                            userFavourite.setUpdatedAt(LocalDateTime.now());

                            userFavouriteRepository.save(userFavourite);
                            savedId.set(userFavourite.getId());
                            message.set("Đã thêm vào danh sách yêu thích: " + request.getName());
                        }
                );

        Map<String, String> data = new HashMap<>();
        data.put("savedId", savedId.get());

        ResDTO<Map<String, String>> response = new ResDTO<>();
        response.setMessage(message.get());
        response.setCode(200);
        response.setData(data);

        return response;
    }

    public ResDTO<?> deleteUserFavourite(String id){
        userFavouriteRepository.deleteById(id);

        ResDTO<?> response = new ResDTO<>();
        response.setData(null);
        response.setCode(200);
        response.setMessage("Đã xóa");

        return response;
    }

}
