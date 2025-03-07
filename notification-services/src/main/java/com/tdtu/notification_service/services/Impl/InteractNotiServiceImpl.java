package com.tdtu.notification_service.services.Impl;

import com.tdtu.notification_service.dtos.ResDTO;
import com.tdtu.notification_service.models.InteractNotification;
import com.tdtu.notification_service.repositories.NotificationRepository;
import com.tdtu.notification_service.services.InteractNotiService;
import com.tdtu.notification_service.utils.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class InteractNotiServiceImpl implements InteractNotiService {

    private final NotificationRepository notificationRepository;
    private final JwtUtils jwtUtils;
    public InteractNotification save(InteractNotification obj){
        return notificationRepository.save(obj);
    }

    public ResDTO<List<InteractNotification>> getAllNotifications(String token) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        List<InteractNotification> notificationsByIdUser = notificationRepository.findByUserId(userId);
        ResDTO<List<InteractNotification>> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setData(notificationsByIdUser);
        response.setMessage("Lấy danh sách notification thành công");
        return response;
    }
    public ResDTO<?> saveNotification(String token, InteractNotification notification) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        notification.setUserId(userId);
        notificationRepository.save(notification);

        ResDTO<?> response = new ResDTO<>();
        response.setCode(HttpServletResponse.SC_OK);
        response.setMessage("Lưu thông báo thành công");
        response.setData(null);
        return response;
    }
    public ResDTO<?> deleteNotification(String token, String id) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        ResDTO<?> response = new ResDTO<>();
        try {
            Optional<InteractNotification> notification = notificationRepository.findById(id);
            if (notification.isEmpty() || !notification.get().getUserId().equals(userId)) {
                response.setCode(HttpServletResponse.SC_FORBIDDEN);
                response.setMessage("Không có quyền xóa thông báo này");
                return response;
            }
            notificationRepository.deleteByIdAndUserId(id, userId);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Xóa thành công theo id");
        } catch (IllegalArgumentException e) {
            response.setCode(HttpServletResponse.SC_BAD_REQUEST);
            response.setMessage("ID không hợp lệ");
        } catch (Exception e) {
            response.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Lỗi khi xóa thông báo: " + e.getMessage());
        }
        response.setData(null);
        return response;
    }

    public ResDTO<?> deleteAllNotifications(String token) {
        String userId = jwtUtils.getUserIdFromJwtToken(token);
        ResDTO<?> response = new ResDTO<>();
        try {
            if (notificationRepository.findByUserId(userId).isEmpty()) {
                response.setCode(HttpServletResponse.SC_NOT_FOUND);
                response.setMessage("Không có thông báo nào để xóa");
                return response;
            }
            notificationRepository.deleteAllByUserId(userId);
            response.setCode(HttpServletResponse.SC_OK);
            response.setMessage("Xóa thành công tất cả thông báo");
        } catch (Exception e) {
            response.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Lỗi khi xóa tất cả thông báo: " + e.getMessage());
        }
        response.setData(null);
        return response;
    }
}