package com.jobportal.backend.repository;

import com.jobportal.backend.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByCandidateUserEmailOrderByCreatedAtDesc(
            String email
    );
}