package com.tdtu.friend_recommendation_services.service;

import com.tdtu.friend_recommendation_services.dto.ResDTO;
import com.tdtu.friend_recommendation_services.dto.request.SaveUserFavouriteDTO;

public interface IUserFavouriteService {
    public ResDTO<?> saveUserFavorite(String token, SaveUserFavouriteDTO request);
    public ResDTO<?> deleteUserFavourite(String id);
}
