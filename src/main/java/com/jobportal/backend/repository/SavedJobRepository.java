package com.jobportal.backend.repository;

import com.jobportal.backend.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository
        extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByCandidateUserEmail(String email);

    boolean existsByCandidateUserEmailAndJobId(
            String email,
            Long jobId
    );

    Optional<SavedJob> findByCandidateUserEmailAndJobId(
            String email,
            Long jobId
    );
}