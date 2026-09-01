package com.jobportal.backend.service;

import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;

    public JobService(
            JobRepository jobRepository,
            RecruiterProfileRepository recruiterProfileRepository) {

        this.jobRepository = jobRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    // CREATE JOB
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

        job.setRecruiter(recruiter);

        return jobRepository.save(job);
    }

    // GET ALL JOBS
    public List<JobEntity> getAllJobs() {

        return jobRepository.findAll();
    }

    // GET RECRUITER JOBS
    public List<JobEntity> getRecruiterJobs(
            String recruiterEmail) {

        return jobRepository
                .findByRecruiterUserEmail(recruiterEmail);
    }

    // GET JOB BY ID
    public JobEntity getJobById(Long id) {

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found with id: " + id
                        )
                );
    }

    // UPDATE JOB
    public JobEntity updateJob(
            Long id,
            JobEntity updatedJob,
            String recruiterEmail) {

        JobEntity existingJob =
                jobRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found with id: " + id
                                )
                        );

        if (!existingJob.getRecruiter()
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
        existingJob.setSalary(updatedJob.getSalary());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setSkills(updatedJob.getSkills());

        return jobRepository.save(existingJob);
    }

    // DELETE JOB
    public void deleteJob(
            Long id,
            String recruiterEmail) {

        JobEntity existingJob =
                jobRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found with id: " + id
                                )
                        );

        if (!existingJob.getRecruiter()
                .getUser()
                .getEmail()
                .equals(recruiterEmail)) {

            throw new RuntimeException(
                    "You are not authorized to delete this job"
            );
        }

        jobRepository.delete(existingJob);
    }
}