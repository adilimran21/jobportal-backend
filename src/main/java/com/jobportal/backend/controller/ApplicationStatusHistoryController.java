package com.jobportal.backend.controller;

import com.jobportal.backend.dto.ApplicationStatusHistoryResponseDTO;
import com.jobportal.backend.service.ApplicationService;
import com.jobportal.backend.service.ApplicationStatusHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate/applications")
@CrossOrigin(origins = "*")
public class ApplicationStatusHistoryController {

    private final ApplicationStatusHistoryService historyService;
    private final ApplicationService applicationService;

    public ApplicationStatusHistoryController(
            ApplicationStatusHistoryService historyService,
            ApplicationService applicationService) {

        this.historyService = historyService;
        this.applicationService = applicationService;
    }

    @GetMapping("/{applicationId}/timeline")
    public ResponseEntity<List<ApplicationStatusHistoryResponseDTO>> getApplicationTimeline(
            @PathVariable Long applicationId,
            Authentication authentication) {

        String email = authentication.getName();

        applicationService.getCandidateApplicationById(
                applicationId,
                email);

        return ResponseEntity.ok(
                historyService.getApplicationTimeline(applicationId));
    }
}