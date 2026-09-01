package com.jobportal.backend.service;

import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    private final RecruiterProfileRepository recruiterProfileRepository;

    public RecruiterProfileService(
            RecruiterProfileRepository recruiterProfileRepository) {

        this.recruiterProfileRepository = recruiterProfileRepository;
    }

    // GET RECRUITER PROFILE BY EMAIL
    public Optional<RecruiterProfile> getProfileByEmail(String email) {

        return recruiterProfileRepository.findByUserEmail(email);
    }

    // CREATE RECRUITER PROFILE
    public RecruiterProfile createProfile(
            RecruiterProfile profile,
            UserEntity user) {

        profile.setUser(user);

        return recruiterProfileRepository.save(profile);
    }

    // UPDATE RECRUITER PROFILE
    public RecruiterProfile updateProfile(
            RecruiterProfile existingProfile,
            RecruiterProfile profile) {

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

        return recruiterProfileRepository.save(existingProfile);
    }
}