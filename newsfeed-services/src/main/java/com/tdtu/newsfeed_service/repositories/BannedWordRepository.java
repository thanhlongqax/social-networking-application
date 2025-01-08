package com.tdtu.newsfeed_service.repositories;


import com.tdtu.newsfeed_service.models.BannedWord;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface BannedWordRepository extends MongoRepository<BannedWord, String> {
    boolean existsByWord(String word);
    Optional<BannedWord> findByWord(String word);
}