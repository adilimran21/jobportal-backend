package com.jobportal.backend.service;

import com.jobportal.backend.dto.CandidateProfileResponseDTO;
import com.jobportal.backend.dto.SavedJobJobResponseDTO;
import com.jobportal.backend.dto.SavedJobResponseDTO;
import com.jobportal.backend.entity.CandidateProfile;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.entity.SavedJob;
import com.jobportal.backend.exception.DuplicateSavedJobException;
import com.jobportal.backend.repository.CandidateProfileRepository;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.SavedJobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final JobRepository jobRepository;

    public SavedJobService(
            SavedJobRepository savedJobRepository,
            CandidateProfileRepository candidateProfileRepository,
            JobRepository jobRepository) {

        this.savedJobRepository = savedJobRepository;
        this.candidateProfileRepository = candidateProfileRepository;
        this.jobRepository = jobRepository;
    }

    public SavedJobResponseDTO saveJob(
            Long jobId,
            String candidateEmail) {

        CandidateProfile candidate =
                candidateProfileRepository
                        .findByUserEmail(candidateEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Candidate profile not found"
                                )
                        );

        JobEntity job =
                jobRepository
                        .findById(jobId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Job not found"
                                )
                        );

        if (savedJobRepository
                .existsByCandidateUserEmailAndJobId(
                        candidateEmail,
                        jobId
                )) {

            throw new DuplicateSavedJobException(
                    "Job already saved"
            );
        }

        SavedJob savedJob = new SavedJob();

        savedJob.setCandidate(candidate);
        savedJob.setJob(job);

        SavedJob saved =
                savedJobRepository.save(savedJob);

        return convertToDTO(saved);
    }

    public List<SavedJobResponseDTO> getSavedJobs(
            String candidateEmail) {

        return savedJobRepository
                .findByCandidateUserEmail(candidateEmail)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public void deleteSavedJob(
            Long jobId,
            String candidateEmail) {

        SavedJob savedJob =
                savedJobRepository
                        .findByCandidateUserEmailAndJobId(
                                candidateEmail,
                                jobId
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Saved job not found"
                                )
                        );

        savedJobRepository.delete(savedJob);
    }

    private SavedJobResponseDTO convertToDTO(
            SavedJob savedJob) {

        CandidateProfile candidate =
                savedJob.getCandidate();

        JobEntity job =
                savedJob.getJob();

        CandidateProfileResponseDTO candidateDTO =
                new CandidateProfileResponseDTO(
                        candidate.getId(),
                        candidate.getUser().getName(),
                        candidate.getUser().getEmail(),
                        candidate.getPhone(),
                        candidate.getLocation(),
                        candidate.getSkills(),
                        candidate.getExperience(),
                        candidate.getEducation(),
                        candidate.getResume()
                );

        SavedJobJobResponseDTO jobDTO =
                new SavedJobJobResponseDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation(),
                        job.getDescription(),
                        job.getResponsibilities(),
                        job.getSalary(),
                        job.getJobType(),
                        job.getSkills(),
                        job.getGoodToHave(),
                        job.getQualifications(),
                        job.getExperience(),
                        job.getContractType(),
                        job.getWorkMode(),
                        job.getVacancies(),
                        job.getApplicationDeadline(),
                        job.getPostedDate(),
                        job.getStatus()
                );

        return new SavedJobResponseDTO(
                savedJob.getId(),
                candidateDTO,
                jobDTO
        );
    }
}