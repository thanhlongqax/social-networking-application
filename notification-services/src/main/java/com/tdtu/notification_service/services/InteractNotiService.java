package com.tdtu.notification_service.services;

import com.tdtu.notification_service.models.InteractNotification;
import org.springframework.stereotype.Service;

@Service
public interface InteractNotiService {
    public InteractNotification save(InteractNotification obj);
}
