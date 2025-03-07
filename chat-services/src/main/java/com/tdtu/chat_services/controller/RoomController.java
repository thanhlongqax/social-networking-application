package com.tdtu.chat_services.controller;

import com.tdtu.chat_services.dto.ResDTO;
import com.tdtu.chat_services.service.IRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/rooms")
@Tag(name = "Chat Service", description = "API For Chat")
@RequiredArgsConstructor
public class RoomController {
    private final IRoomService roomService;
    @GetMapping()
    public ResponseEntity<?> getRooms(@RequestHeader("Authorization") String token){
        ResDTO<?> response = roomService.findRoomsByToken(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
