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

    // --------------------------------------------------
    // GET CANDIDATE PROFILE
    // --------------------------------------------------

    public CandidateProfileResponseDTO getProfile(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        /*
         * Candidate may be visiting the profile page
         * for the first time.
         *
         * In that case a CandidateProfile record may not
         * exist yet.
         *
         * We return an empty profile DTO containing the
         * logged-in user's basic information instead of
         * throwing 404.
         */

        CandidateProfile profile =
                candidateProfileRepository.findByUser(user)
                        .orElse(null);

        if (profile == null) {

            return new CandidateProfileResponseDTO(
                    null,
                    user.getName(),
                    user.getEmail(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    "fresher",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        return convertToResponseDTO(profile);
    }

    // --------------------------------------------------
    // CREATE / UPDATE CANDIDATE PROFILE
    // --------------------------------------------------

    public CandidateProfileResponseDTO saveProfile(
            String email,
            CandidateProfileRequestDTO requestDTO) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        /*
         * If profile already exists -> update it.
         *
         * If profile does not exist -> create a new one.
         */

        CandidateProfile profile =
                candidateProfileRepository.findByUser(user)
                        .orElse(new CandidateProfile());

        // Update user's name
        if (requestDTO.getName() != null &&
                !requestDTO.getName().isBlank()) {

            user.setName(requestDTO.getName().trim());
            userRepository.save(user);
        }

        // Connect profile with logged-in user
        profile.setUser(user);

        // Candidate information
        profile.setPhone(requestDTO.getPhone());
        profile.setLocation(requestDTO.getLocation());

        // Professional information
        profile.setProfileImage(requestDTO.getProfileImage());
        profile.setJobTitle(requestDTO.getJobTitle());
        profile.setHeadline(requestDTO.getHeadline());
        profile.setCareerStatus(requestDTO.getCareerStatus());
        profile.setExperience(requestDTO.getExperience());
        profile.setCurrentCompany(requestDTO.getCurrentCompany());
        profile.setCurrentSalary(requestDTO.getCurrentSalary());
        profile.setSkills(requestDTO.getSkills());

        // Education
        profile.setTenthMarks(requestDTO.getTenthMarks());
        profile.setTwelfthMarks(requestDTO.getTwelfthMarks());
        profile.setGraduation(requestDTO.getGraduation());

        // About / links
        profile.setAbout(requestDTO.getAbout());
        profile.setLinkedin(requestDTO.getLinkedin());
        profile.setPortfolio(requestDTO.getPortfolio());

        // Resume
        profile.setResume(requestDTO.getResume());

        CandidateProfile savedProfile =
                candidateProfileRepository.save(profile);

        return convertToResponseDTO(savedProfile);
    }

    // --------------------------------------------------
    // ENTITY → RESPONSE DTO
    // --------------------------------------------------

    private CandidateProfileResponseDTO convertToResponseDTO(
            CandidateProfile profile) {

        return new CandidateProfileResponseDTO(
                profile.getId(),
                profile.getUser().getName(),
                profile.getUser().getEmail(),
                profile.getPhone(),
                profile.getLocation(),
                profile.getProfileImage(),
                profile.getJobTitle(),
                profile.getHeadline(),
                profile.getCareerStatus(),
                profile.getExperience(),
                profile.getCurrentCompany(),
                profile.getCurrentSalary(),
                profile.getSkills(),
                profile.getTenthMarks(),
                profile.getTwelfthMarks(),
                profile.getGraduation(),
                profile.getAbout(),
                profile.getLinkedin(),
                profile.getPortfolio(),
                profile.getResume()
        );
    }
}