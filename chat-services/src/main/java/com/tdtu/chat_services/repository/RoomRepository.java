package com.tdtu.chat_services.repository;


import com.tdtu.chat_services.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Optional<Room> findByUserId1AndUserId2OrUserId2AndUserId1(String userId1, String userId2, String userId22, String userId12);
    List<Room> findByUserId1OrUserId2(String userId1, String userId2);
}
