package com.jobportal.backend.controller;

import com.jobportal.backend.dto.NotificationResponseDTO;
import com.jobportal.backend.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>>
    getNotifications(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                notificationService.getCandidateNotifications(email)
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();

        notificationService.markAsRead(id, email);

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }
}