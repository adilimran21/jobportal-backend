package com.jobportal.backend.service;

import com.jobportal.backend.dto.JobRecommendationResponseDTO;
import com.jobportal.backend.entity.CandidateProfile;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.repository.CandidateProfileRepository;
import com.jobportal.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobRecommendationService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final JobRepository jobRepository;

    public JobRecommendationService(
            CandidateProfileRepository candidateProfileRepository,
            JobRepository jobRepository) {

        this.candidateProfileRepository = candidateProfileRepository;
        this.jobRepository = jobRepository;
    }

    public List<JobRecommendationResponseDTO> getRecommendedJobs(
            String email) {

        CandidateProfile profile = candidateProfileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "Candidate profile not found"));

        List<JobEntity> jobs = jobRepository.findAll();

        List<JobRecommendationResponseDTO> recommendations = new ArrayList<>();

        for (JobEntity job : jobs) {

            int skillScore = calculateSkillScore(
                    profile.getSkills(),
                    job.getSkills());

            int experienceScore = calculateExperienceScore(
                    profile.getExperience(),
                    job.getExperience());

            int locationScore = calculateLocationScore(
                    profile.getLocation(),
                    job.getLocation());

            int matchScore = skillScore +
                    experienceScore +
                    locationScore;

            if (matchScore > 0) {

                recommendations.add(
                        new JobRecommendationResponseDTO(
                                job.getId(),
                                job.getTitle(),
                                job.getCompany(),
                                matchScore));
            }
        }

        recommendations.sort(
                (a, b) -> Integer.compare(
                        b.getMatchScore(),
                        a.getMatchScore()));

        return recommendations;
    }

    private int calculateSkillScore(
            String candidateSkills,
            String jobSkills) {

        if (candidateSkills == null ||
                jobSkills == null ||
                candidateSkills.isBlank() ||
                jobSkills.isBlank()) {

            return 0;
        }

        String[] candidateSkillArray = candidateSkills.toLowerCase()
                .split(",");

        String[] jobSkillArray = jobSkills.toLowerCase()
                .split(",");

        int matchedSkills = 0;

        for (String candidateSkill : candidateSkillArray) {

            String candidateSkillClean = candidateSkill.trim();

            for (String jobSkill : jobSkillArray) {

                String jobSkillClean = jobSkill.trim();

                if (candidateSkillClean.equals(
                        jobSkillClean)) {

                    matchedSkills++;
                    break;
                }
            }
        }

        if (candidateSkillArray.length == 0) {
            return 0;
        }

        return (matchedSkills * 60)
                / candidateSkillArray.length;
    }

    private int calculateExperienceScore(
            String candidateExperience,
            String jobExperience) {

        if (candidateExperience == null ||
                jobExperience == null ||
                candidateExperience.isBlank() ||
                jobExperience.isBlank()) {

            return 0;
        }

        double candidateYears = convertExperienceToYears(
                candidateExperience);

        double jobMinYears = extractMinimumYears(
                jobExperience);

        double jobMaxYears = extractMaximumYears(
                jobExperience);

        if (candidateYears >= jobMinYears &&
                candidateYears <= jobMaxYears) {

            return 25;
        }

        return 0;
    }

    private double convertExperienceToYears(
            String experience) {

        String value = experience.trim().toLowerCase();

        if (value.contains("month")) {

            String number = value.replaceAll(
                    "[^0-9.]",
                    "");

            if (!number.isBlank()) {
                return Double.parseDouble(number) / 12;
            }
        }

        if (value.contains("year")) {

            String number = value.replaceAll(
                    "[^0-9.]",
                    "");

            if (!number.isBlank()) {
                return Double.parseDouble(number);
            }
        }

        return 0;
    }

    private double extractMinimumYears(
            String experience) {

        String value = experience.trim().toLowerCase();

        String[] parts = value.split("-");

        if (parts.length > 0) {

            String number = parts[0].replaceAll(
                    "[^0-9.]",
                    "");

            if (!number.isBlank()) {
                return Double.parseDouble(number);
            }
        }

        return convertExperienceToYears(
                experience);
    }

    private double extractMaximumYears(
            String experience) {

        String value = experience.trim().toLowerCase();

        String[] parts = value.split("-");

        if (parts.length > 1) {

            String number = parts[1].replaceAll(
                    "[^0-9.]",
                    "");

            if (!number.isBlank()) {
                return Double.parseDouble(number);
            }
        }

        return convertExperienceToYears(
                experience);
    }

    private int calculateLocationScore(
            String candidateLocation,
            String jobLocation) {

        if (candidateLocation == null ||
                jobLocation == null ||
                candidateLocation.isBlank() ||
                jobLocation.isBlank()) {

            return 0;
        }

        if (candidateLocation.trim()
                .equalsIgnoreCase(
                        jobLocation.trim())) {

            return 15;
        }

        return 0;
    }
}