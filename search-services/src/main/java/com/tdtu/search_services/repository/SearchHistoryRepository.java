package com.tdtu.search_services.repository;

import com.tdtu.search_services.model.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {
    List<SearchHistory> findByUserIdOrderByCreatedAtDesc(String userId);
}

