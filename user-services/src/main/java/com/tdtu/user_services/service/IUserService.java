package com.tdtu.user_services.service;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.dto.request.*;
import com.tdtu.user_services.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService {
    public ResDTO<?> findAll();
    public ResDTO<?> findByToken(String token);
    public ResDTO<?> findByEmailResp(String email);
    public User findByEmail(String email);
    public ResDTO<?> findResById(String id);
    public ResDTO<?> findResByIds(FindByIdsReqDTO request);
    public User findById(String id);
    public ResDTO<?> findByUserName(String username);
    public ResDTO<?> existsById(String id);
    public ResDTO<?> searchByName(String token , String keyword);
    public ResDTO<?> saveUser(SaveUserReqDTO user);
    public ResDTO<?> updateBio(String token, UpdateBioReqDTO userBio);
    public ResDTO<?> updateProfile(String token, ProfileDTO profileDTO);
    public ResDTO<?> renameUser(String token, RenameReqDTO request);
    public ResDTO<?> updatePicture(String token, MultipartFile pic, boolean isProfilePic);
    public ResDTO<?> updateGender(String token, UpdateGenderReqDTO userGender);
    public ResDTO<?> disableAccount(DisableAccountReqDTO account);
    public ResDTO<?> saveUserRegistrationId(String token, SaveUserResIdReq requestBody);


}
