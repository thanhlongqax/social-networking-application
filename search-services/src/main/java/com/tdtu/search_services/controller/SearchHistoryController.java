package com.tdtu.search_services.controller;


import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.service.IUserSearchService;
import com.tdtu.search_services.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search-history")
@RequiredArgsConstructor
@Tag(name = "Search History Service", description = "API For Search History")
public class SearchHistoryController {
    private final SearchHistoryService searchHistoryService;

    // Lưu lịch sử tìm kiếm
    @PostMapping("/create")
    public ResponseEntity<?> createSearchHistory(@RequestHeader("Authorization") String token, @RequestParam String keyword) {
        ResDTO<?>  response = searchHistoryService.saveSearchHistory(token, keyword);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // Lấy danh sách lịch sử tìm kiếm
    @GetMapping("/list")
    public ResponseEntity<?> getUserSearchHistory(@RequestHeader("Authorization") String token) {

        ResDTO<?> response = searchHistoryService.getUserSearchHistory(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // Xóa một lịch sử tìm kiếm theo ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSearchHistory(@PathVariable String id) {
        ResDTO<?>  response =  searchHistoryService.deleteSearchHistoryById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    // Xóa toàn bộ lịch sử tìm kiếm
    @DeleteMapping("/delete-all")
    public ResponseEntity<?> deleteAllSearchHistory(@RequestHeader("Authorization") String token) {
        ResDTO<?> response = searchHistoryService.deleteAllSearchHistory(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}

