package com.jobportal.backend.service;

import com.jobportal.backend.dto.JobStatisticsResponseDTO;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

@Service
public class JobStatisticsService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public JobStatisticsService(
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    public JobStatisticsResponseDTO getStatistics(
            String recruiterEmail) {

        long totalJobs =
                jobRepository.countByRecruiterUserEmail(
                        recruiterEmail
                );

        long totalApplications =
                applicationRepository.countByJobRecruiterUserEmail(
                        recruiterEmail
                );

        long pendingApplications =
                applicationRepository
                        .countByJobRecruiterUserEmailAndStatus(
                                recruiterEmail,
                                ApplicationStatus.APPLIED
                        );

        long shortlistedApplications =
                applicationRepository
                        .countByJobRecruiterUserEmailAndStatus(
                                recruiterEmail,
                                ApplicationStatus.SHORTLISTED
                        );

        long rejectedApplications =
                applicationRepository
                        .countByJobRecruiterUserEmailAndStatus(
                                recruiterEmail,
                                ApplicationStatus.REJECTED
                        );

        long hiredApplications =
                applicationRepository
                        .countByJobRecruiterUserEmailAndStatus(
                                recruiterEmail,
                                ApplicationStatus.HIRED
                        );

        return new JobStatisticsResponseDTO(
                totalJobs,
                totalApplications,
                pendingApplications,
                shortlistedApplications,
                rejectedApplications,
                hiredApplications
        );
    }
}