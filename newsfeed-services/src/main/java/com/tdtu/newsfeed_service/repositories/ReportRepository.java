package com.tdtu.newsfeed_service.repositories;



import com.tdtu.newsfeed_service.models.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends MongoRepository<Report, String> {
}