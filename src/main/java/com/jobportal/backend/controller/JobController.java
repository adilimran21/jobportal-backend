package com.jobportal.backend.controller;

import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // RECRUITER - CREATE JOB
    @PostMapping("/recruiter/jobs")
    public ResponseEntity<JobEntity> createJob(
            @RequestBody JobEntity job,
            Authentication authentication) {

        String recruiterEmail =
                authentication.getName();

        JobEntity savedJob =
                jobService.createJob(
                        job,
                        recruiterEmail
                );

        return ResponseEntity.ok(savedJob);
    }

    // JOB SEEKER - GET ALL JOBS
    @GetMapping("/jobseeker/jobs")
    public ResponseEntity<List<JobEntity>> getAllJobs() {

        return ResponseEntity.ok(
                jobService.getAllJobs()
        );
    }

    // RECRUITER - GET OWN JOBS
    @GetMapping("/recruiter/jobs")
    public ResponseEntity<List<JobEntity>> getRecruiterJobs(
            Authentication authentication) {

        String recruiterEmail =
                authentication.getName();

        return ResponseEntity.ok(
                jobService.getRecruiterJobs(
                        recruiterEmail
                )
        );
    }

    // GET JOB BY ID
    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobEntity> getJobById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                jobService.getJobById(id)
        );
    }

    // UPDATE JOB
    @PutMapping("/recruiter/jobs/{id}")
    public ResponseEntity<JobEntity> updateJob(
            @PathVariable Long id,
            @RequestBody JobEntity job,
            Authentication authentication) {

        String recruiterEmail =
                authentication.getName();

        return ResponseEntity.ok(
                jobService.updateJob(
                        id,
                        job,
                        recruiterEmail
                )
        );
    }

    // DELETE JOB
    @DeleteMapping("/recruiter/jobs/{id}")
    public ResponseEntity<String> deleteJob(
            @PathVariable Long id,
            Authentication authentication) {

        String recruiterEmail =
                authentication.getName();

        jobService.deleteJob(
                id,
                recruiterEmail
        );

        return ResponseEntity.ok(
                "Job deleted successfully"
        );
    }
}