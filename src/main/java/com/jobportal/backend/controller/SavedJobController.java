package com.jobportal.backend.controller;

import com.jobportal.backend.dto.SavedJobResponseDTO;
import com.jobportal.backend.service.SavedJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/saved-jobs")
@CrossOrigin(origins = "*")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(
            SavedJobService savedJobService) {

        this.savedJobService = savedJobService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<SavedJobResponseDTO> saveJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String candidateEmail = authentication.getName();

        SavedJobResponseDTO savedJob =
                savedJobService.saveJob(
                        jobId,
                        candidateEmail);

        return ResponseEntity.ok(savedJob);
    }

    @GetMapping
    public ResponseEntity<List<SavedJobResponseDTO>> getSavedJobs(
            Authentication authentication) {

        String candidateEmail = authentication.getName();

        return ResponseEntity.ok(
                savedJobService.getSavedJobs(
                        candidateEmail));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<String> deleteSavedJob(
            @PathVariable Long jobId,
            Authentication authentication) {

        String candidateEmail = authentication.getName();

        savedJobService.deleteSavedJob(
                jobId,
                candidateEmail);

        return ResponseEntity.ok(
                "Job removed from saved jobs");
    }
}