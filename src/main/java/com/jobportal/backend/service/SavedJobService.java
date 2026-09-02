package com.jobportal.backend.service;

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

    public SavedJob saveJob(Long jobId, String candidateEmail) {

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

        return savedJobRepository.save(savedJob);
    }

    public List<SavedJob> getSavedJobs(
            String candidateEmail) {

        return savedJobRepository
                .findByCandidateUserEmail(candidateEmail);
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
}