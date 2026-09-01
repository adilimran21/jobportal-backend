package com.jobportal.backend.service;

import com.jobportal.backend.dto.CandidateProfileRequestDTO;
import com.jobportal.backend.dto.CandidateProfileResponseDTO;
import com.jobportal.backend.entity.CandidateProfile;
import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.CandidateProfileRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserRepository userRepository;

    public CandidateProfileService(
            CandidateProfileRepository candidateProfileRepository,
            UserRepository userRepository) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.userRepository = userRepository;
    }

    // GET CANDIDATE PROFILE
    public CandidateProfileResponseDTO getProfile(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUser(user)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate profile not found"));

        return convertToResponseDTO(profile);
    }

    // CREATE / UPDATE PROFILE
    public CandidateProfileResponseDTO saveProfile(
            String email,
            CandidateProfileRequestDTO requestDTO) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        CandidateProfile profile =
                candidateProfileRepository.findByUser(user)
                        .orElse(new CandidateProfile());

        profile.setUser(user);
        profile.setPhone(requestDTO.getPhone());
        profile.setLocation(requestDTO.getLocation());
        profile.setSkills(requestDTO.getSkills());
        profile.setExperience(requestDTO.getExperience());
        profile.setEducation(requestDTO.getEducation());
        profile.setResume(requestDTO.getResume());

        CandidateProfile savedProfile =
                candidateProfileRepository.save(profile);

        return convertToResponseDTO(savedProfile);
    }

    // ENTITY → RESPONSE DTO
    private CandidateProfileResponseDTO convertToResponseDTO(
            CandidateProfile profile) {

        return new CandidateProfileResponseDTO(
                profile.getId(),
                profile.getUser().getName(),
                profile.getUser().getEmail(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getSkills(),
                profile.getExperience(),
                profile.getEducation(),
                profile.getResume()
        );
    }
}