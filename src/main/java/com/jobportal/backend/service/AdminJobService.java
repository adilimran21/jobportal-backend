package com.jobportal.backend.service;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.repository.JobCategoryRepository;
import com.jobportal.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminJobService {

    private final JobRepository jobRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public AdminJobService(
            JobRepository jobRepository,
            JobCategoryRepository jobCategoryRepository) {

        this.jobRepository = jobRepository;
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<JobEntity> getAllJobs() {

        return jobRepository.findAll();
    }

    public JobEntity getJobById(Long jobId) {

        return jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));
    }

    public JobEntity updateJob(
            Long jobId,
            JobEntity updatedJob) {

        JobEntity existingJob = jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setCompany(updatedJob.getCompany());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setResponsibilities(
                updatedJob.getResponsibilities());
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setSkills(updatedJob.getSkills());
        existingJob.setGoodToHave(
                updatedJob.getGoodToHave());
        existingJob.setQualifications(
                updatedJob.getQualifications());
        existingJob.setExperience(
                updatedJob.getExperience());
        existingJob.setContractType(
                updatedJob.getContractType());
        existingJob.setWorkMode(
                updatedJob.getWorkMode());
        existingJob.setVacancies(
                updatedJob.getVacancies());
        existingJob.setApplicationDeadline(
                updatedJob.getApplicationDeadline());
        existingJob.setPostedDate(
                updatedJob.getPostedDate());

        if (updatedJob.getCategory() != null) {

            Long categoryId =
                    updatedJob.getCategory().getId();

            JobCategory category =
                    jobCategoryRepository
                            .findById(categoryId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Category not found"));

            existingJob.setCategory(category);
        }

        return jobRepository.save(existingJob);
    }

    public JobEntity removeJob(Long jobId) {

        JobEntity existingJob = jobRepository
                .findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("Job not found"));

        existingJob.setStatus("REMOVED");

        return jobRepository.save(existingJob);
    }
}