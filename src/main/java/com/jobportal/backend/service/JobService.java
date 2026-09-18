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
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.jobCategoryRepository = jobCategoryRepository;
    }

   
    // CREATE JOB
   

    public JobEntity createJob(
            JobEntity job,
            String recruiterEmail) {

        RecruiterProfile recruiter = recruiterProfileRepository
                .findByUserEmail(recruiterEmail)
                .orElseThrow(() -> new RuntimeException(
                        "Recruiter profile not found for email: "
                                + recruiterEmail));

       
        // HANDLE CATEGORY
       

        if (job.getCategory() != null) {

            JobCategory requestCategory = job.getCategory();

            JobCategory category = null;

            // Existing category selected by ID
            if (requestCategory.getId() != null) {

                category = jobCategoryRepository
                        .findById(requestCategory.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));
            }

            // New category entered by recruiter
            else if (
                    requestCategory.getName() != null &&
                    !requestCategory.getName()
                            .trim()
                            .isEmpty()) {

                String categoryName =
                        requestCategory
                                .getName()
                                .trim();

                category = jobCategoryRepository
                        .findByName(categoryName)
                        .orElseGet(() -> {

                            JobCategory newCategory =
                                    new JobCategory();

                            newCategory.setName(categoryName);

                            newCategory.setDescription(
                                    "Jobs related to "
                                            + categoryName
                                            + ".");

                            return jobCategoryRepository
                                    .save(newCategory);
                        });
            }

            if (category != null) {
                job.setCategory(category);
            }
        }

       
        // SET RECRUITER
       

        job.setRecruiter(recruiter);

       
        // DEFAULT STATUS
       

        job.setStatus("ACTIVE");

       
        // SAVE JOB
       

        return jobRepository.save(job);
    }

   
    // GET ALL ACTIVE JOBS
   

    public List<JobEntity> getAllJobs() {

        return jobRepository.findByStatus("ACTIVE");
    }

   
    // GET JOB BY ID
   

    public JobEntity getJobById(Long jobId) {

        return jobRepository
                .findJobByIdWithCategory(jobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found"));
    }

   
    // GET RECRUITER JOBS
   

    public List<JobEntity> getRecruiterJobs(
            String recruiterEmail) {

        return jobRepository
                .findByRecruiterUserEmail(recruiterEmail);
    }

   
    // UPDATE JOB
   

    public JobEntity updateJob(
            Long jobId,
            JobEntity updatedJob,
            String recruiterEmail) {

        /*
         * IMPORTANT:
         * Fetch recruiter + user together.
         * This prevents LazyInitializationException
         * during authorization.
         */
        JobEntity existingJob = jobRepository
                .findJobByIdWithRecruiter(jobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found"));

       
        // AUTHORIZATION CHECK
       

        if (!existingJob
                .getRecruiter()
                .getUser()
                .getEmail()
                .equals(recruiterEmail)) {

            throw new RuntimeException(
                    "You are not authorized to update this job");
        }

       
        // UPDATE BASIC DETAILS
       

        existingJob.setTitle(
                updatedJob.getTitle());

        existingJob.setCompany(
                updatedJob.getCompany());

        existingJob.setLocation(
                updatedJob.getLocation());

        existingJob.setDescription(
                updatedJob.getDescription());

        existingJob.setResponsibilities(
                updatedJob.getResponsibilities());

        existingJob.setSalary(
                updatedJob.getSalary());

        existingJob.setJobType(
                updatedJob.getJobType());

        existingJob.setSkills(
                updatedJob.getSkills());

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

       
        // HANDLE CATEGORY UPDATE
       

        if (updatedJob.getCategory() != null) {

            JobCategory requestCategory =
                    updatedJob.getCategory();

            JobCategory category = null;

            // Existing category
            if (requestCategory.getId() != null) {

                category = jobCategoryRepository
                        .findById(
                                requestCategory.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"));
            }

            // New category
            else if (
                    requestCategory.getName() != null &&
                    !requestCategory
                            .getName()
                            .trim()
                            .isEmpty()) {

                String categoryName =
                        requestCategory
                                .getName()
                                .trim();

                category = jobCategoryRepository
                        .findByName(categoryName)
                        .orElseGet(() -> {

                            JobCategory newCategory =
                                    new JobCategory();

                            newCategory.setName(
                                    categoryName);

                            newCategory.setDescription(
                                    "Jobs related to "
                                            + categoryName
                                            + ".");

                            return jobCategoryRepository
                                    .save(newCategory);
                        });
            }

            if (category != null) {
                existingJob.setCategory(category);
            }
        }

        return jobRepository.save(existingJob);
    }

   
    // DELETE JOB
   

    public void deleteJob(
            Long jobId,
            String recruiterEmail) {

        /*
         * IMPORTANT:
         * Do NOT use findById() here because recruiter
         * is LAZY in JobEntity.
         *
         * findJobByIdWithRecruiter() fetches:
         * Job → RecruiterProfile → User
         * in the same query/session.
         */
        JobEntity existingJob = jobRepository
                .findJobByIdWithRecruiter(jobId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Job not found"));

       
        // AUTHORIZATION CHECK
       

        if (!existingJob
                .getRecruiter()
                .getUser()
                .getEmail()
                .equals(recruiterEmail)) {

            throw new RuntimeException(
                    "You are not authorized to delete this job");
        }

       
        // SOFT DELETE
       

        existingJob.setStatus("REMOVED");

        jobRepository.save(existingJob);
    }
}