package com.jobportal.backend.service;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.repository.JobCategoryRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public JobService(
            JobRepository jobRepository,
            RecruiterProfileRepository recruiterProfileRepository,
            JobCategoryRepository jobCategoryRepository) {

        this.jobRepository = jobRepository;
        this.recruiterProfileRepository =
                recruiterProfileRepository;
        this.jobCategoryRepository =
                jobCategoryRepository;
    }

    public JobEntity createJob(
            JobEntity job,
            String recruiterEmail) {

        RecruiterProfile recruiter =
                recruiterProfileRepository
                        .findByUserEmail(recruiterEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter profile not found for email: "
                                                + recruiterEmail
                                )
                        );

        if (job.getCategory() != null) {

            Long categoryId =
                    job.getCategory().getId();

            JobCategory category =
                    jobCategoryRepository
                            .findById(categoryId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Category not found"
                                    )
                            );

            job.setCategory(category);
        }

        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    public List<JobEntity> getAllJobs() {

        return jobRepository.findAll();
    }

    public List<JobEntity> getRecruiterJobs(
            String recruiterEmail) {

        return jobRepository
                .findByRecruiterUserEmail(recruiterEmail);
    }

    public JobEntity updateJob(
            Long jobId,
            JobEntity updatedJob,
            String recruiterEmail) {

        JobEntity existingJob =
                jobRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"
                                )
                        );

        if (!existingJob
                .getRecruiter()
                .getUser()
                .getEmail()
                .equals(recruiterEmail)) {

            throw new RuntimeException(
                    "You are not authorized to update this job"
            );
        }

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setCompany(updatedJob.getCompany());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setResponsibilities(
                updatedJob.getResponsibilities()
        );
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setSkills(updatedJob.getSkills());
        existingJob.setGoodToHave(
                updatedJob.getGoodToHave()
        );
        existingJob.setQualifications(
                updatedJob.getQualifications()
        );
        existingJob.setExperience(
                updatedJob.getExperience()
        );
        existingJob.setContractType(
                updatedJob.getContractType()
        );
        existingJob.setWorkMode(
                updatedJob.getWorkMode()
        );
        existingJob.setVacancies(
                updatedJob.getVacancies()
        );
        existingJob.setApplicationDeadline(
                updatedJob.getApplicationDeadline()
        );
        existingJob.setPostedDate(
                updatedJob.getPostedDate()
        );

        if (updatedJob.getCategory() != null) {

            Long categoryId =
                    updatedJob.getCategory().getId();

            JobCategory category =
                    jobCategoryRepository
                            .findById(categoryId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Category not found"
                                    )
                            );

            existingJob.setCategory(category);
        }

        return jobRepository.save(existingJob);
    }
}