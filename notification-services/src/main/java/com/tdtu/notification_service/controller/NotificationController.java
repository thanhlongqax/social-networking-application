package com.tdtu.notification_service.controller;


import com.tdtu.notification_service.dtos.ResDTO;
import com.tdtu.notification_service.models.InteractNotification;
import com.tdtu.notification_service.services.InteractNotiService;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Service", description = "API For User")
public class NotificationController {
    private final InteractNotiService interactNotiService;

    @GetMapping
    public ResponseEntity<?> getAllNotifications(@RequestHeader("Authorization") String token) {
        ResDTO<?> response = interactNotiService.getAllNotifications(token);
        return ResponseEntity.status(response.getCode()).body(response);

    }

    @PostMapping
    public ResponseEntity<?> saveNotification(@RequestHeader("Authorization") String token,
                                              @RequestBody InteractNotification notification) {


        ResDTO<?> response = interactNotiService.saveNotification(token, notification);
        return ResponseEntity.status(response.getCode()).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @RequestHeader("Authorization") String token,
            @PathVariable String id) {

        ResDTO<?> response = interactNotiService.deleteNotification(token, id);
        return ResponseEntity.status(response.getCode()).body(response);

    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllNotifications(@RequestHeader("Authorization") String token) {

        ResDTO<?> response = interactNotiService.deleteAllNotifications(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
