package com.tdtu.notification_service.services;

import com.tdtu.notification_service.dtos.ResDTO;
import com.tdtu.notification_service.models.InteractNotification;


public interface InteractNotiService {
    public InteractNotification save(InteractNotification obj);
    public ResDTO<?> getAllNotifications(String token );
    public ResDTO<?> deleteNotification(String token , String id);
    public ResDTO<?> deleteAllNotifications(String token );
    public ResDTO<?> saveNotification(String token, InteractNotification notification);
}
