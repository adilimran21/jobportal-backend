package com.jobportal.backend.controller;

import com.jobportal.backend.dto.CandidateProfileRequestDTO;
import com.jobportal.backend.dto.CandidateProfileResponseDTO;
import com.jobportal.backend.service.CandidateProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin(origins = "*")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(
            CandidateProfileService candidateProfileService) {

        this.candidateProfileService = candidateProfileService;
    }

    // GET CANDIDATE PROFILE

    @GetMapping("/profile")
    public ResponseEntity<CandidateProfileResponseDTO> getProfile(
            Authentication authentication) {

        String email = authentication.getName();

        CandidateProfileResponseDTO profile =
                candidateProfileService.getProfile(email);

        return ResponseEntity.ok(profile);
    }
    
 // CREATE / UPDATE PROFILE

    @PutMapping("/profile")
    public ResponseEntity<CandidateProfileResponseDTO> saveProfile(
            @RequestBody CandidateProfileRequestDTO requestDTO,
            Authentication authentication) {

        String email = authentication.getName();

        CandidateProfileResponseDTO profile =
                candidateProfileService.saveProfile(
                        email,
                        requestDTO
                );

        return ResponseEntity.ok(profile);
    }
}