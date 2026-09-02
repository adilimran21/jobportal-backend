package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository
        extends JpaRepository<Application, Long> {

    List<Application> findByCandidateUserEmail(String email);

    List<Application> findByJobRecruiterUserEmail(String email);

    boolean existsByCandidateUserEmailAndJobId(
            String email,
            Long jobId
    );

    Optional<Application> findByIdAndCandidateUserEmail(
            Long id,
            String email
    );

    Optional<Application> findByIdAndJobRecruiterUserEmail(
            Long id,
            String email
    );

    long countByJobRecruiterUserEmail(String email);

    long countByJobRecruiterUserEmailAndStatus(
            String email,
            ApplicationStatus status
    );
}