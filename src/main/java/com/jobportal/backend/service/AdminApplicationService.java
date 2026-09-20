package com.jobportal.backend.service;

import com.jobportal.backend.dto.ApplicationResponseDTO;
import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminApplicationService {

    private final ApplicationRepository applicationRepository;

    public AdminApplicationService(
            ApplicationRepository applicationRepository) {

        this.applicationRepository = applicationRepository;
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponseDTO> getAllApplications() {

        return applicationRepository
                .findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    public Map<String, Long> getApplicationStatistics() {

        Map<String, Long> statistics = new HashMap<>();

        statistics.put(
                "totalApplications",
                applicationRepository.count());

        statistics.put(
                "applied",
                applicationRepository.countByStatus(
                        ApplicationStatus.APPLIED));

        statistics.put(
                "shortlisted",
                applicationRepository.countByStatus(
                        ApplicationStatus.SHORTLISTED));

        statistics.put(
                "rejected",
                applicationRepository.countByStatus(
                        ApplicationStatus.REJECTED));

        statistics.put(
                "hired",
                applicationRepository.countByStatus(
                        ApplicationStatus.HIRED));

        return statistics;
    }

    private ApplicationResponseDTO convertToResponseDTO(
            Application application) {

        return new ApplicationResponseDTO(
                application.getId(),
                application.getJob().getId(),
                application.getJob().getTitle(),
                application.getJob().getCompany(),
                application.getCandidate().getUser().getName(),
                application.getCandidate().getUser().getEmail(),
                application.getStatus().name(),
                application.getAppliedAt());
    }
}