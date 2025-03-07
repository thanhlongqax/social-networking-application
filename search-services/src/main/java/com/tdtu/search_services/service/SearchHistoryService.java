package com.tdtu.search_services.service;

import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.model.SearchHistory;

import java.util.List;

public interface SearchHistoryService {
    public ResDTO<?> saveSearchHistory(String token, String keyword);
    public ResDTO<List<SearchHistory>> getUserSearchHistory(String token);
    public ResDTO<?> deleteSearchHistoryById(String id);
    public ResDTO<?> deleteAllSearchHistory(String token);
}
