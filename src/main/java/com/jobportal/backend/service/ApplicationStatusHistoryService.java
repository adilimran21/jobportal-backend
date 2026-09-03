package com.jobportal.backend.service;

import com.jobportal.backend.dto.ApplicationStatusHistoryResponseDTO;
import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.entity.ApplicationStatusHistoryEntity;
import com.jobportal.backend.repository.ApplicationStatusHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationStatusHistoryService {

    private final ApplicationStatusHistoryRepository historyRepository;

    public ApplicationStatusHistoryService(
            ApplicationStatusHistoryRepository historyRepository) {

        this.historyRepository = historyRepository;
    }

    public void addHistory(
            Application application,
            ApplicationStatus status) {

        ApplicationStatusHistoryEntity history =
                new ApplicationStatusHistoryEntity();

        history.setApplication(application);
        history.setStatus(status);
        history.setChangedAt(LocalDateTime.now());

        historyRepository.save(history);
    }

    public List<ApplicationStatusHistoryResponseDTO>
    getApplicationTimeline(Long applicationId) {

        return historyRepository
                .findByApplicationIdOrderByChangedAtAsc(applicationId)
                .stream()
                .map(history -> new ApplicationStatusHistoryResponseDTO(
                        history.getId(),
                        history.getStatus(),
                        history.getChangedAt()
                ))
                .toList();
    }
}