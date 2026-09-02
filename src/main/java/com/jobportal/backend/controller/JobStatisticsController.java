package com.jobportal.backend.controller;

import com.jobportal.backend.dto.JobStatisticsResponseDTO;
import com.jobportal.backend.service.JobStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/statistics")
@CrossOrigin(origins = "*")
public class JobStatisticsController {

    private final JobStatisticsService jobStatisticsService;

    public JobStatisticsController(
            JobStatisticsService jobStatisticsService) {

        this.jobStatisticsService = jobStatisticsService;
    }

    @GetMapping
    public ResponseEntity<JobStatisticsResponseDTO> getStatistics(
            Authentication authentication) {

        String recruiterEmail = authentication.getName();

        return ResponseEntity.ok(
                jobStatisticsService.getStatistics(
                        recruiterEmail));
    }
}