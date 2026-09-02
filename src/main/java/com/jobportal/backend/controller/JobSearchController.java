package com.jobportal.backend.controller;

import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.service.JobSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobSearchController {

    private final JobSearchService jobSearchService;

    public JobSearchController(JobSearchService jobSearchService) {
        this.jobSearchService = jobSearchService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<JobEntity>> searchJobs(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) String location,

            @RequestParam(required = false) String jobType,

            @RequestParam(required = false) String experience,

            @RequestParam(required = false) String workMode,

            @RequestParam(required = false) String contractType,

            @RequestParam(required = false) Long categoryId) {

        List<JobEntity> jobs = jobSearchService.searchJobs(
                keyword,
                location,
                jobType,
                experience,
                workMode,
                contractType,
                categoryId);

        return ResponseEntity.ok(jobs);
    }
}