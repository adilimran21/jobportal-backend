package com.jobportal.backend.controller;

import com.jobportal.backend.dto.JobRecommendationResponseDTO;
import com.jobportal.backend.service.JobRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin(origins = "*")
public class JobRecommendationController {

    private final JobRecommendationService jobRecommendationService;

    public JobRecommendationController(
            JobRecommendationService jobRecommendationService) {

        this.jobRecommendationService = jobRecommendationService;
    }

    @GetMapping("/recommended-jobs")
    public ResponseEntity<List<JobRecommendationResponseDTO>> getRecommendedJobs(
            Authentication authentication) {

        String email = authentication.getName();

        List<JobRecommendationResponseDTO> recommendations =
                jobRecommendationService.getRecommendedJobs(email);

        return ResponseEntity.ok(recommendations);
    }
}