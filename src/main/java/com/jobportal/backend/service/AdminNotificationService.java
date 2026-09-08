package com.jobportal.backend.service;

import com.jobportal.backend.entity.AdminNotificationEntity;
import com.jobportal.backend.repository.AdminNotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminNotificationService {

    private final AdminNotificationRepository adminNotificationRepository;

    public AdminNotificationService(
            AdminNotificationRepository adminNotificationRepository) {

        this.adminNotificationRepository = adminNotificationRepository;
    }

    public List<AdminNotificationEntity> getAllNotifications() {

        return adminNotificationRepository
                .findAllByOrderByCreatedAtDesc();
    }

    public AdminNotificationEntity createNotification(
            String message,
            String type) {

        AdminNotificationEntity notification =
                new AdminNotificationEntity();

        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return adminNotificationRepository.save(notification);
    }

    public void markAsRead(Long notificationId) {

        AdminNotificationEntity notification =
                adminNotificationRepository.findById(notificationId)
                        .orElseThrow(() -> new RuntimeException(
                                "Admin notification not found"));

        notification.setRead(true);

        adminNotificationRepository.save(notification);
    }
}