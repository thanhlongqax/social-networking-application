package com.tdtu.notification_service.services.Impl;

import com.tdtu.notification_service.models.InteractNotification;
import com.tdtu.notification_service.repositories.NotificationRepository;
import com.tdtu.notification_service.services.InteractNotiService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class InteractNotiServiceImpl implements InteractNotiService {
    @Autowired
    private final NotificationRepository repository;
    public InteractNotification save(InteractNotification obj){
        return repository.save(obj);
    }
}