package com.jobportal.backend.controller;

import com.jobportal.backend.dto.ApplicationRequestDTO;
import com.jobportal.backend.dto.ApplicationResponseDTO;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApplicationController {

        private final ApplicationService applicationService;

        public ApplicationController(
                        ApplicationService applicationService) {
                this.applicationService = applicationService;
        }

        @PostMapping("/candidate/applications")
        public ResponseEntity<ApplicationResponseDTO> applyForJob(
                        @RequestBody ApplicationRequestDTO requestDTO,
                        Authentication authentication) {

                String email = authentication.getName();

                ApplicationResponseDTO response = applicationService.applyForJob(
                                email,
                                requestDTO.getJobId());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/candidate/applications")
        public ResponseEntity<List<ApplicationResponseDTO>> getCandidateApplications(
                        Authentication authentication) {

                String email = authentication.getName();

                return ResponseEntity.ok(
                                applicationService.getCandidateApplications(
                                                email));
        }

        @GetMapping("/candidate/applications/{id}")
        public ResponseEntity<ApplicationResponseDTO> getCandidateApplicationById(
                        @PathVariable Long id,
                        Authentication authentication) {

                String email = authentication.getName();

                return ResponseEntity.ok(
                                applicationService.getCandidateApplicationById(
                                                id,
                                                email));
        }

        @GetMapping("/recruiter/applications")
        public ResponseEntity<List<ApplicationResponseDTO>> getRecruiterApplications(
                        Authentication authentication) {

                String email = authentication.getName();

                return ResponseEntity.ok(
                                applicationService.getRecruiterApplications(
                                                email));
        }

        @GetMapping("/recruiter/applications/{id}")
        public ResponseEntity<ApplicationResponseDTO> getRecruiterApplicationById(
                        @PathVariable Long id,
                        Authentication authentication) {

                String email = authentication.getName();

                return ResponseEntity.ok(
                                applicationService.getRecruiterApplicationById(
                                                id,
                                                email));
        }

        @PutMapping("/recruiter/applications/{id}/status")
        public ResponseEntity<ApplicationResponseDTO> updateApplicationStatus(
                        @PathVariable Long id,
                        @RequestParam ApplicationStatus status,
                        Authentication authentication) {

                String recruiterEmail = authentication.getName();

                return ResponseEntity.ok(
                                applicationService.updateStatus(
                                                id,
                                                status,
                                                recruiterEmail));
        }
}