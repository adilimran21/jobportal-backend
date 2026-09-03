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

        @PostMapping("/recruiter/jobs")
        public ResponseEntity<JobEntity> createJob(
                        @RequestBody JobEntity job,
                        Authentication authentication) {

                String recruiterEmail = authentication.getName();

                JobEntity savedJob = jobService.createJob(
                                job,
                                recruiterEmail);

                return ResponseEntity.ok(savedJob);
        }

        @GetMapping("/jobseeker/jobs")
        public ResponseEntity<List<JobEntity>> getAllJobs() {

                return ResponseEntity.ok(
                                jobService.getAllJobs());
        }

        @GetMapping("/jobseeker/jobs/{id}")
        public ResponseEntity<JobEntity> getJobById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                jobService.getJobById(id));
        }

        @GetMapping("/recruiter/jobs")
        public ResponseEntity<List<JobEntity>> getRecruiterJobs(
                        Authentication authentication) {

                String recruiterEmail = authentication.getName();

                return ResponseEntity.ok(
                                jobService.getRecruiterJobs(
                                                recruiterEmail));
        }

        @PutMapping("/recruiter/jobs/{id}")
        public ResponseEntity<JobEntity> updateJob(
                        @PathVariable Long id,
                        @RequestBody JobEntity job,
                        Authentication authentication) {

                String recruiterEmail = authentication.getName();

                JobEntity updatedJob = jobService.updateJob(
                                id,
                                job,
                                recruiterEmail);

                return ResponseEntity.ok(updatedJob);
        }
}