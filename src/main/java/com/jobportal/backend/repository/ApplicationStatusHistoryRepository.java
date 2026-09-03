package com.jobportal.backend.repository;

import com.jobportal.backend.entity.ApplicationStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationStatusHistoryRepository
        extends JpaRepository<ApplicationStatusHistoryEntity, Long> {

    List<ApplicationStatusHistoryEntity>
    findByApplicationIdOrderByChangedAtAsc(Long applicationId);
}