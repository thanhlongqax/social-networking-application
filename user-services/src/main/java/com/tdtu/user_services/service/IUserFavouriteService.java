package com.tdtu.user_services.service;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.dto.request.SaveUserFavouriteDTO;

public interface IUserFavouriteService {
    public ResDTO<?> saveUserFavorite(String token, SaveUserFavouriteDTO request);
    public ResDTO<?> deleteUserFavourite(String id);
}
