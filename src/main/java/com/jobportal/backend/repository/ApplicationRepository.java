package com.jobportal.backend.repository;

import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository
                extends JpaRepository<Application, Long> {

        @Query("""
                        SELECT a
                        FROM Application a
                        JOIN FETCH a.job j
                        JOIN FETCH a.candidate c
                        JOIN FETCH c.user u
                        WHERE u.email = :email
                        """)
        List<Application> findByCandidateUserEmail(
                        @Param("email") String email);

        @Query("""
                        SELECT a
                        FROM Application a
                        JOIN FETCH a.job j
                        JOIN FETCH a.candidate c
                        JOIN FETCH c.user u
                        JOIN FETCH j.recruiter r
                        JOIN FETCH r.user ru
                        WHERE ru.email = :email
                        """)
        List<Application> findByJobRecruiterUserEmail(
                        @Param("email") String email);

        boolean existsByCandidateUserEmailAndJobId(
                        String email,
                        Long jobId);

        @Query("""
                        SELECT a
                        FROM Application a
                        JOIN FETCH a.job j
                        JOIN FETCH a.candidate c
                        JOIN FETCH c.user u
                        WHERE a.id = :id
                        AND u.email = :email
                        """)
        Optional<Application> findByIdAndCandidateUserEmail(
                        @Param("id") Long id,
                        @Param("email") String email);

        @Query("""
                        SELECT a
                        FROM Application a
                        JOIN FETCH a.job j
                        JOIN FETCH a.candidate c
                        JOIN FETCH c.user u
                        JOIN FETCH j.recruiter r
                        JOIN FETCH r.user ru
                        WHERE a.id = :id
                        AND ru.email = :email
                        """)
        Optional<Application> findByIdAndJobRecruiterUserEmail(
                        @Param("id") Long id,
                        @Param("email") String email);

        long countByJobRecruiterUserEmail(String email);

        long countByJobRecruiterUserEmailAndStatus(
                        String email,
                        ApplicationStatus status);

        long countByStatus(ApplicationStatus status);
}