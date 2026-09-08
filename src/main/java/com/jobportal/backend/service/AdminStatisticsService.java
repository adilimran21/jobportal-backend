package com.jobportal.backend.service;

import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminStatisticsService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminStatisticsService(
            UserRepository userRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    public Map<String, Long> getDashboardStatistics() {

        Map<String, Long> statistics = new HashMap<>();

        statistics.put(
                "totalUsers",
                userRepository.count());

        statistics.put(
                "totalCandidates",
                userRepository.countByRole("JOB_SEEKER"));

        statistics.put(
                "totalRecruiters",
                userRepository.countByRole("RECRUITER"));

        statistics.put(
                "totalJobs",
                jobRepository.count());

        statistics.put(
                "totalApplications",
                applicationRepository.count());

        return statistics;
    }
}