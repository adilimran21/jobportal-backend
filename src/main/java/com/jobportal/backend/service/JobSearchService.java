package com.jobportal.backend.service;

import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSearchService {

    private final JobRepository jobRepository;

    public JobSearchService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobEntity> searchJobs(
            String keyword,
            String location,
            String jobType,
            String experience,
            String workMode,
            String contractType,
            Long categoryId
    ) {

        return jobRepository.searchJobs(
                keyword,
                location,
                jobType,
                experience,
                workMode,
                contractType,
                categoryId
        );
    }
}