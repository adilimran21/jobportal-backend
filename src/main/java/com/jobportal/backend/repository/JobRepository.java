package com.jobportal.backend.repository;

import com.jobportal.backend.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JobRepository
        extends JpaRepository<JobEntity, Long> {

    // =========================================================
    // RECRUITER JOBS
    // Only ACTIVE jobs are returned.
    // Category is fetched together with the job.
    // =========================================================

    @Query("""
            SELECT j
            FROM JobEntity j
            LEFT JOIN FETCH j.category
            WHERE j.recruiter.user.email = :email
            AND j.status = 'ACTIVE'
            ORDER BY j.id DESC
            """)
    List<JobEntity> findByRecruiterUserEmail(
            @Param("email") String email);


    // =========================================================
    // RECRUITER
    // =========================================================

    List<JobEntity> findByRecruiterId(
            Long recruiterId);


    List<JobEntity> findByRecruiterIdAndStatus(
            Long recruiterId,
            String status);


    // =========================================================
    // JOB COUNTS
    // =========================================================

    long countByRecruiterUserEmail(
            String email);


    long countByRecruiterUserEmailAndStatus(
            String email,
            String status);


    long countByStatus(
            String status);


    // =========================================================
    // ACTIVE / STATUS JOBS
    // =========================================================

    @Query("""
            SELECT j
            FROM JobEntity j
            LEFT JOIN FETCH j.category
            WHERE j.status = :status
            """)
    List<JobEntity> findByStatus(
            @Param("status") String status);


    // =========================================================
    // FIND JOB BY ID WITH CATEGORY
    // =========================================================

    @Query("""
            SELECT j
            FROM JobEntity j
            LEFT JOIN FETCH j.category
            WHERE j.id = :id
            """)
    Optional<JobEntity> findJobByIdWithCategory(
            @Param("id") Long id);


    // =========================================================
    // FIND JOB BY ID WITH RECRUITER + USER + CATEGORY
    // Used for recruiter update/delete authorization.
    // =========================================================

    @Query("""
            SELECT j
            FROM JobEntity j
            LEFT JOIN FETCH j.recruiter r
            LEFT JOIN FETCH r.user u
            LEFT JOIN FETCH j.category
            WHERE j.id = :id
            """)
    Optional<JobEntity> findJobByIdWithRecruiter(
            @Param("id") Long id);


    // =========================================================
    // JOB SEARCH
    // =========================================================

    @Query("""
            SELECT j
            FROM JobEntity j
            LEFT JOIN FETCH j.category
            WHERE
            j.status = 'ACTIVE'
            AND (
                :keyword IS NULL
                OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            AND (
                :location IS NULL
                OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))
            )
            AND (
                :jobType IS NULL
                OR LOWER(j.jobType) = LOWER(:jobType)
            )
            AND (
                :experience IS NULL
                OR LOWER(j.experience) = LOWER(:experience)
            )
            AND (
                :workMode IS NULL
                OR LOWER(j.workMode) = LOWER(:workMode)
            )
            AND (
                :contractType IS NULL
                OR LOWER(j.contractType) = LOWER(:contractType)
            )
            AND (
                :categoryId IS NULL
                OR j.category.id = :categoryId
            )
            ORDER BY j.postedDate DESC
            """)
    List<JobEntity> searchJobs(
            @Param("keyword") String keyword,
            @Param("location") String location,
            @Param("jobType") String jobType,
            @Param("experience") String experience,
            @Param("workMode") String workMode,
            @Param("contractType") String contractType,
            @Param("categoryId") Long categoryId
    );
}