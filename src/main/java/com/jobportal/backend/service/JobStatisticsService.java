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

        // JOB STATISTICS

        // Total jobs ever posted by this recruiter
        long totalJobs =
                jobRepository.countByRecruiterUserEmail(
                        recruiterEmail
                );

        // Currently active jobs
        long activeJobs =
                jobRepository.countByRecruiterUserEmailAndStatus(
                        recruiterEmail,
                        "ACTIVE"
                );

        // Removed jobs
        long removedJobs =
                jobRepository.countByRecruiterUserEmailAndStatus(
                        recruiterEmail,
                        "REMOVED"
                );

        // APPLICATION STATISTICS

        long totalApplications =
                applicationRepository
                        .countByJobRecruiterUserEmail(
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

        // RESPONSE

        return new JobStatisticsResponseDTO(
                totalJobs,
                activeJobs,
                removedJobs,
                totalApplications,
                pendingApplications,
                shortlistedApplications,
                rejectedApplications,
                hiredApplications
        );
    }
}