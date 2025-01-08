package com.tdtu.newsfeed_service.controller;



import com.tdtu.newsfeed_service.dtos.ResDTO;
import com.tdtu.newsfeed_service.dtos.request.CreateBannedWordReq;
import com.tdtu.newsfeed_service.services.BannedWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/banned-word")
@RequiredArgsConstructor
public class BannedWordController {
    private final BannedWordService bannedWordService;

    @PostMapping
    public ResponseEntity<?> createBannedWord(@RequestBody CreateBannedWordReq requestBody){
        ResDTO<?> response = bannedWordService.saveBannedWord(requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeBannedWord(@RequestBody CreateBannedWordReq requestBody){
        ResDTO<?> response = bannedWordService.removeBannedWord(requestBody);

        return ResponseEntity.status(response.getCode()).body(response);
    }
}
