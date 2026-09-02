package com.jobportal.backend.service;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.repository.JobCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    public JobCategoryService(
            JobCategoryRepository jobCategoryRepository) {

        this.jobCategoryRepository = jobCategoryRepository;
    }

    public JobCategory createCategory(
            JobCategory category) {

        if (jobCategoryRepository
                .existsByName(category.getName())) {

            throw new RuntimeException(
                    "Category already exists"
            );
        }

        return jobCategoryRepository.save(category);
    }

    public List<JobCategory> getAllCategories() {

        return jobCategoryRepository.findAll();
    }

    public JobCategory getCategoryById(
            Long id) {

        return jobCategoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Category not found"
                        )
                );
    }

    public void deleteCategory(Long id) {

        JobCategory category =
                jobCategoryRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Category not found"
                                )
                        );

        jobCategoryRepository.delete(category);
    }
}