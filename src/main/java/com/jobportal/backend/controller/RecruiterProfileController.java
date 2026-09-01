package com.jobportal.backend.controller;

import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter/profile")
@CrossOrigin(origins = "*")
public class RecruiterProfileController {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final UserRepository userRepository;

    public RecruiterProfileController(
            RecruiterProfileRepository recruiterProfileRepository,
            UserRepository userRepository) {

        this.recruiterProfileRepository = recruiterProfileRepository;
        this.userRepository = userRepository;
    }

    // CREATE / UPDATE RECRUITER PROFILE
    @PostMapping
    public ResponseEntity<RecruiterProfile> createOrUpdateProfile(
            @RequestBody RecruiterProfile profile,
            Authentication authentication) {

        String email = authentication.getName();

        // Find logged-in user
        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with email: " + email
                        )
                );

        // Find existing recruiter profile using user email
        RecruiterProfile existingProfile =
                recruiterProfileRepository
                        .findByUserEmail(email)
                        .orElse(null);

        // UPDATE existing profile
        if (existingProfile != null) {

            existingProfile.setCompanyName(
                    profile.getCompanyName()
            );

            existingProfile.setCompanyLocation(
                    profile.getCompanyLocation()
            );

            existingProfile.setCompanyDescription(
                    profile.getCompanyDescription()
            );

            existingProfile.setWebsite(
                    profile.getWebsite()
            );

            return ResponseEntity.ok(
                    recruiterProfileRepository.save(existingProfile)
            );
        }

        // CREATE new profile
        profile.setUser(user);

        return ResponseEntity.ok(
                recruiterProfileRepository.save(profile)
        );
    }

    // GET RECRUITER PROFILE
    @GetMapping
    public ResponseEntity<RecruiterProfile> getProfile(
            Authentication authentication) {

        String email = authentication.getName();

        RecruiterProfile profile =
                recruiterProfileRepository
                        .findByUserEmail(email)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recruiter profile not found"
                                )
                        );

        return ResponseEntity.ok(profile);
    }
}