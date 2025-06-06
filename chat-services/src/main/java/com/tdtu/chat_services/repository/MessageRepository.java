package com.tdtu.chat_services.repository;


import com.tdtu.chat_services.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findAllByToUserId(String toUserId);
}