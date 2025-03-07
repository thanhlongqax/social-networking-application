package com.tdtu.user_services.service.Impl;

import com.tdtu.user_services.dto.ResDTO;
import com.tdtu.user_services.dto.request.*;
import com.tdtu.user_services.dto.respone.AuthUserResponse;
import com.tdtu.user_services.dto.respone.MinimizedUserResponse;
import com.tdtu.user_services.dto.respone.SaveUserResponse;
import com.tdtu.user_services.enums.EFileType;
import com.tdtu.user_services.enums.EUserRole;
import com.tdtu.user_services.mapper.request.SaveUserReqMapper;
import com.tdtu.user_services.mapper.respone.MinimizedUserMapper;
import com.tdtu.user_services.model.User;
import com.tdtu.user_services.repository.UserRepository;
import com.tdtu.user_services.service.IUserSearchService;
import com.tdtu.user_services.service.IUserService;
import com.tdtu.user_services.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final SaveUserReqMapper saveUserReqMapper;
    private final FileService fileService;
    private final JwtUtils jwtUtils;
    private final FirebaseService firebaseService;
    private final MinimizedUserMapper minimizedUserMapper;
    private final IUserSearchService userSearchService;
    public ResDTO<?> findAll(){
        ResDTO<List<User>> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("users fetched successfully");
        response.setData(userRepository.findByActive(true));
        return response;
    }

    public ResDTO<?> findByToken(String token){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        log.info(userId + " ");
        ResDTO<User> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("users fetched successfully");
        response.setData(userRepository.findByIdAndActive(userId, true).orElse(null));

        return response;
    }
    public ResDTO<?> findByUserName(String username){
        ResDTO<User> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("users fetched successfully");
        response.setData(userRepository.findByUsernameAndActive(username, true).orElse(null));
        return response;
    }
//    private void saveUserToElasticSearch(User user) {
//        UserDocument userDocument = new UserDocument();
//        userDocument.setId(user.getId());
//        userDocument.setUsername(user.getUsername());
//        userDocument.setFirstName(user.getFirstName());
//        userDocument.setMiddleName(user.getMiddleName());
//        userDocument.setLastName(user.getLastName());
//        userDocument.setUserFullName(user.getUserFullName());
//
//        userSearchRepository.save(userDocument);
//    }
    public ResDTO<?> findByEmailResp(String email){
        User foundUser = findByEmail(email);
        ResDTO<AuthUserResponse> response = new ResDTO<>();

        if(foundUser != null) {
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("User fetched successfully");
            response.setData(AuthUserResponse.builder()
                    .id(foundUser.getId())
                    .password(foundUser.getHashPassword())
                    .email(foundUser.getEmail())
                    .active(foundUser.isActive())
                    .username(foundUser.getUsername())
                    .userFullName(foundUser.getUserFullName())
                    .role(foundUser.getRole() == null ? EUserRole.ROLE_USER : foundUser.getRole())
                    .build());
        }else{
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setMessage("User not found with email: " + email);
            response.setData(null);
        }

        return response;
    }

    public User findByEmail(String email){
        return userRepository.findByEmailAndActive(email, true).orElse(null);
    }

    public ResDTO<?> findResById(String id){
        ResDTO<MinimizedUserResponse> response = new ResDTO<>();

        User foundUser = userRepository.findByIdAndActive(id, true).orElse(null);

        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("user fetched successfully");
        response.setData(minimizedUserMapper.mapToDTO(foundUser));

        return response;
    }

    public ResDTO<?> findResByIds(FindByIdsReqDTO request){
        ResDTO<List<MinimizedUserResponse>> response = new ResDTO<>();

        List<MinimizedUserResponse> users = userRepository.findByIdInAndActive(request.getUserIds(), true)
                .stream().map(minimizedUserMapper::mapToDTO)
                .toList();

        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("users fetched successfully");
        response.setData(users);

        return response;
    }

    public User findById(String id){
        return userRepository.findByIdAndActive(id, true).orElse(null);
    }

    public ResDTO<?> existsById(String id){
        ResDTO<Boolean> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("success");
        response.setData(userRepository.existsById(id));
        return response;
    }

    public ResDTO<?> searchByName(String token , String keyword) {
        jwtUtils.getTokenSubject(token);
        ResDTO<List<MinimizedUserResponse>> response = new ResDTO<>();

        List<MinimizedUserResponse> userResponses = userRepository
                .searchUsers(keyword) // Tìm trong DB trước
                .stream()
                .filter(user -> user.getUserFullName().toLowerCase().contains(keyword.toLowerCase())) // Lọc thêm với userFullName
                .map(minimizedUserMapper::mapToDTO)
                .toList();

        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("success");
        response.setData(userResponses);
        return response;
    }

    public ResDTO<?> saveUser(SaveUserReqDTO user){
        ResDTO<SaveUserResponse> response = new ResDTO<>();
        if(!userRepository.existsByEmail(user.getEmail())){
            User savedUser = userRepository.save(saveUserReqMapper.mapToObject(user));

            userSearchService.saveUserToElasticSearch(savedUser);

            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Đăng ký thành công");
            response.setData(SaveUserResponse.builder()
                    .id(savedUser.getId())
                    .email(savedUser.getEmail())
                    .build());

        }else{
            response.setData(null);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setMessage("Email này đã tồn tại!123");
        }

        return response;
    }

    public ResDTO<?> updateBio(String token, UpdateBioReqDTO userBio){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        User user = findById(userId);
        ResDTO<User> response = new ResDTO<>();
        if(user != null){
            user.setBio(userBio.getBio());

            response.setMessage("Cập nhật thành công!");
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(userRepository.save(user));
        }else{
            response.setMessage("Không tìm thấy người dùng với id: " + userId);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
        }

        return response;
    }
    public ResDTO<?> updateProfile(String token, ProfileDTO profileDTO){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        User user = findById(userId);
        ResDTO<User> response = new ResDTO<>();
        if(user != null){
            user.setFirstName(profileDTO.getFirstName());
            user.setLastName(profileDTO.getLastName());
            user.setProfilePicture(profileDTO.getProfilePicture());
            user.setUsername(profileDTO.getUsername());
            user.setGender(profileDTO.getGender());
            user.setBirthday(profileDTO.getBirthday());
            user.setBio(profileDTO.getBio());

            response.setMessage("Cập nhật thành công!");
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(userRepository.save(user));
        }else{
            response.setMessage("Không tìm thấy người dùng với id: " + userId);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
        }

        return response;
    }
    public ResDTO<?> renameUser(String token, RenameReqDTO request){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        User user = findById(userId);
        ResDTO<User> response = new ResDTO<>();
        if(user != null){
            user.setFirstName(request.getFirstName());
            user.setMiddleName(request.getMiddleName());
            user.setLastName(request.getLastName());

            response.setMessage("Cập nhật thành công!");
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(userRepository.save(user));
        }else{
            response.setMessage("Không tìm thấy người dùng với id " + userId);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
        }

        return response;
    }

    public ResDTO<?> updatePicture(String token, MultipartFile pic, boolean isProfilePic){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        User foundUser = findById(userId);
        ResDTO<User> response = new ResDTO<>();

        if(foundUser != null){
            try{
                String fileName = fileService.upload(pic, EFileType.TYPE_IMG);

                if(isProfilePic)
                    foundUser.setProfilePicture(fileName);
                else
                    foundUser.setCover(fileName);

                response.setData(userRepository.save(foundUser));
                response.setCode(HttpServletResponse.SC_OK);
                response.setMessage("Cập nhật thành công!");
            }catch (Exception e){
                response.setData(null);
                response.setCode(HttpServletResponse.SC_BAD_REQUEST);
                response.setMessage("Lỗi xảy ra: " + e.getMessage());
            }
        }else{
            response.setData(null);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setMessage("Không tìm thấy người dùng với id: " + userId);
        }

        return response;
    }

    public ResDTO<?> updateGender(String token, UpdateGenderReqDTO userGender){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        User user = findById(userId);
        ResDTO<User> response = new ResDTO<>();
        if(user != null){
            user.setGender(userGender.getGender());

            response.setMessage("Cập nhật thành công");
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(userRepository.save(user));
        }else{
            response.setMessage("Không tìm thấy người dùng với id: " + userId);
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
        }

        return response;
    }

    public ResDTO<?> disableAccount(DisableAccountReqDTO account){
        User user = findById(account.getUserId());
        ResDTO<User> response = new ResDTO<>();
        if(user != null){
            user.setActive(false);

            response.setMessage("Đã vô hiệu hóa tài khoản");
            response.setCode(HttpServletResponse.SC_OK);
            response.setData(userRepository.save(user));
        }else{
            response.setMessage("Không tìm thấy người dùng với id: " + account.getUserId());
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setData(null);
        }

        return response;
    }

    public ResDTO<?> saveUserRegistrationId(String token, SaveUserResIdReq requestBody){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        User foundUser = findById(userId);
        Map<String, String> data = new HashMap<>();
        data.put("notificationKey", "");

        ResDTO<Map<String, String>> response = new ResDTO<>();

        if(foundUser != null && foundUser.isActive()){
            firebaseService.saveUserDeviceGroup(foundUser, List.of(requestBody.getRegistrationId()));
            userRepository.save(foundUser);
            data.put("notificationKey", foundUser.getNotificationKey());

            response.setCode(200);
            response.setData(data);
            response.setMessage("Saved user registration id");

            return response;
        }

        response.setCode(400);
        response.setData(data);
        response.setMessage("User not found with id: " + userId);

        return response;
    }
}