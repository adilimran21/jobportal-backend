package com.jobportal.backend.repository;

import com.jobportal.backend.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository
        extends JpaRepository<JobEntity, Long> {

    List<JobEntity> findByRecruiterUserEmail(String email);

    List<JobEntity> findByRecruiterId(Long recruiterId);
}