package com.jobportal.backend.repository;

import com.jobportal.backend.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository
        extends JpaRepository<JobEntity, Long> {

    List<JobEntity> findByRecruiterUserEmail(String email);

    List<JobEntity> findByRecruiterId(Long recruiterId);

    long countByRecruiterUserEmail(String email);

    @Query("""
            SELECT j FROM JobEntity j
            WHERE
            (:keyword IS NULL OR
             LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(j.company) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
             LOWER(j.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:location IS NULL OR
                 LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
            AND (:jobType IS NULL OR
                 LOWER(j.jobType) = LOWER(:jobType))
            AND (:experience IS NULL OR
                 LOWER(j.experience) = LOWER(:experience))
            AND (:workMode IS NULL OR
                 LOWER(j.workMode) = LOWER(:workMode))
            AND (:contractType IS NULL OR
                 LOWER(j.contractType) = LOWER(:contractType))
            AND (:categoryId IS NULL OR
                 j.category.id = :categoryId)
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