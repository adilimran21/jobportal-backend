package com.jobportal.backend.service;

import com.jobportal.backend.dto.AdminReportResponseDTO;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminReportService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final ApplicationRepository applicationRepository;

    public AdminReportService(
            UserRepository userRepository,
            JobRepository jobRepository,
            RecruiterProfileRepository recruiterProfileRepository,
            ApplicationRepository applicationRepository) {

        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.applicationRepository = applicationRepository;
    }

    public AdminReportResponseDTO generateReport() {

        AdminReportResponseDTO.UserReport users =
                new AdminReportResponseDTO.UserReport(
                        userRepository.count(),
                        userRepository.countByRole("JOB_SEEKER"),
                        userRepository.countByRole("RECRUITER"),
                        userRepository.countByRole("ADMIN")
                );

        AdminReportResponseDTO.JobReport jobs =
                new AdminReportResponseDTO.JobReport(
                        jobRepository.count(),
                        jobRepository.countByStatus("ACTIVE"),
                        jobRepository.countByStatus("REMOVED")
                );

        AdminReportResponseDTO.CompanyReport companies =
                new AdminReportResponseDTO.CompanyReport(
                        recruiterProfileRepository.count(),
                        recruiterProfileRepository.countByModerationStatus("APPROVED"),
                        recruiterProfileRepository.countByModerationStatus("PENDING"),
                        recruiterProfileRepository.countByModerationStatus("REJECTED")
                );

        AdminReportResponseDTO.ApplicationReport applications =
                new AdminReportResponseDTO.ApplicationReport(
                        applicationRepository.count(),
                        applicationRepository.countByStatus(
                                ApplicationStatus.APPLIED),
                        applicationRepository.countByStatus(
                                ApplicationStatus.SHORTLISTED),
                        applicationRepository.countByStatus(
                                ApplicationStatus.REJECTED),
                        applicationRepository.countByStatus(
                                ApplicationStatus.HIRED)
                );

        return new AdminReportResponseDTO(
                users,
                jobs,
                companies,
                applications
        );
    }
}