package com.jobportal.backend.repository;

import com.jobportal.backend.entity.AdminNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminNotificationRepository
        extends JpaRepository<AdminNotificationEntity, Long> {

    List<AdminNotificationEntity> findAllByOrderByCreatedAtDesc();
}