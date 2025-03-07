package com.tdtu.notification_service.repositories;


import com.tdtu.notification_service.models.InteractNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<InteractNotification, String> {
    List<InteractNotification> findByUserId(String userId);
    void deleteByIdAndUserId(String id, String userId);
    void deleteAllByUserId(String userId);

}