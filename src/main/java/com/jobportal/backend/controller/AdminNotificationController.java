package com.jobportal.backend.controller;

import com.jobportal.backend.entity.AdminNotificationEntity;
import com.jobportal.backend.service.AdminNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
@CrossOrigin(origins = "*")
public class AdminNotificationController {

    private final AdminNotificationService adminNotificationService;

    public AdminNotificationController(
            AdminNotificationService adminNotificationService) {

        this.adminNotificationService = adminNotificationService;
    }

    @GetMapping
    public ResponseEntity<List<AdminNotificationEntity>>
    getAllNotifications() {

        return ResponseEntity.ok(
                adminNotificationService.getAllNotifications());
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(
            @PathVariable Long id) {

        adminNotificationService.markAsRead(id);

        return ResponseEntity.ok(
                "Admin notification marked as read");
    }
}