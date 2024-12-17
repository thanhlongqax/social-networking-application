package com.tdtu.user_services.service.Impl;

import com.tdtu.user_services.dto.respone.ResDTO;
import com.tdtu.user_services.exception.HttpException;
import com.tdtu.user_services.repository.UserRepository;
import com.tdtu.user_services.service.IUserService;
import com.tdtu.user_services.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.tdtu.user_services.model.User;

import java.util.List;

@Component
public class UserService implements IUserService {

    UserRepository userRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserService(UserRepository userRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }
    @Override
    public ResDTO<?> createUser(User user) {
        try {
            userRepository.save(user);
            return new ResDTO<>(200 , "Thêm người dùng thành công" , null);
        }catch (Exception ex){
            throw  new HttpException(HttpStatus.BAD_REQUEST , "Không thể thêm người dùng " +  ex.getMessage());
        }


    }

    @Override
    public  ResDTO<?> updateUser(String userId, User updatedUser) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new HttpException(HttpStatus.NOT_FOUND, "User not found"));

            user.setUsername(updatedUser.getUsername());
            user.setEmail(updatedUser.getEmail());
            user.setHashPassword(updatedUser.getHashPassword());
            userRepository.save(user);
            return new ResDTO<>(
                    200,
                    "User updated successfully",
                    null
            );
        }catch (Exception ex){
            throw  new HttpException(HttpStatus.BAD_REQUEST , "Cập nhật người dùng thất bại" +  ex.getMessage());
        }

    }

    @Override
    public ResDTO<?>  deleteUser(String userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            userRepository.delete(user);
            return new ResDTO<>(200 , "Xóa người dùng thành công" , null);
        }catch (Exception ex){
            throw  new HttpException(HttpStatus.BAD_REQUEST , "Xóa người dùng thấy bại " +  ex.getMessage());
        }

    }

    @Override
    public ResDTO<?> getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ResDTO<>(200 , "Lấy người dùng thành công" , user);

    }

    public ResDTO<?> getAllUser(){
        try {
            ResDTO<List<User>> response = new ResDTO<>();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setMessage("users fetched successfully");
            response.setData(userRepository.findByActive(true));
            return response;
        }catch (Exception ex){
            throw  new HttpException(HttpStatus.BAD_REQUEST , "Lấy danh sách người dùng thất bại" +  ex.getMessage());
        }

    }

    public ResDTO<?> findByToken(String token){
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        ResDTO<User> response = new ResDTO<>();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setMessage("users fetched successfully");
        response.setData(userRepository.findByIdAndActive(userId, true).orElse(null));

        return response;
    }


}
