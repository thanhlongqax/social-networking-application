package com.tdtu.notification_service.repositories;


import com.tdtu.notification_service.models.InteractNotification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<InteractNotification, String> {

}