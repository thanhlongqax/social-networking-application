package com.tdtu.newsfeed_service.controller;

import com.tdtu.newsfeed_service.dtos.ResDTO;
import com.tdtu.newsfeed_service.dtos.request.ReportRequest;
import com.tdtu.newsfeed_service.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final PostService postService;
    @PostMapping()
    public ResponseEntity<?> report(@RequestHeader("Authorization") String token,
                                    @RequestBody ReportRequest requestBody){
        ResDTO<?> response = postService.reportPost(token, requestBody);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
