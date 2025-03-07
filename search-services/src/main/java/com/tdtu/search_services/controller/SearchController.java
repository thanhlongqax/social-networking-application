package com.tdtu.search_services.controller;

import com.tdtu.search_services.dto.ResDTO;
import com.tdtu.search_services.dto.request.UserToElasticSearchDTO;
import com.tdtu.search_services.service.IUserSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Search Service", description = "API For Search")
public class SearchController {
    private final IUserSearchService userSearchService;

    @GetMapping("")
    public ResponseEntity<?> search(
            @RequestHeader("Authorization") String token,
            @RequestParam("keyword") String keyword) throws IOException {
        ResDTO<?> response = userSearchService.searchUsers(token , keyword);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveUserToElasticSearch(@RequestBody UserToElasticSearchDTO dto) throws IOException {
        ResDTO<?> response = userSearchService.saveUserToElasticSearch(dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

}
