package com.jobportal.backend.repository;

import com.jobportal.backend.entity.JobCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobCategoryRepository
        extends JpaRepository<JobCategory, Long> {

    Optional<JobCategory> findByName(String name);

    boolean existsByName(String name);
}