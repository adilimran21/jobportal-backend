package com.jobportal.backend.repository;

import com.jobportal.backend.entity.SavedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository
        extends JpaRepository<SavedJob, Long> {

    @Query("""
            SELECT s
            FROM SavedJob s
            JOIN FETCH s.candidate c
            JOIN FETCH c.user u
            JOIN FETCH s.job j
            WHERE u.email = :email
            """)
    List<SavedJob> findByCandidateUserEmail(
            @Param("email") String email
    );

    boolean existsByCandidateUserEmailAndJobId(
            String email,
            Long jobId
    );

    Optional<SavedJob> findByCandidateUserEmailAndJobId(
            String email,
            Long jobId
    );
}