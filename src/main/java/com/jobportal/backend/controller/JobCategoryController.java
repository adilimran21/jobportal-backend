package com.jobportal.backend.controller;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.service.JobCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class JobCategoryController {

    private final JobCategoryService jobCategoryService;

    public JobCategoryController(
            JobCategoryService jobCategoryService) {

        this.jobCategoryService = jobCategoryService;
    }

    @PostMapping
    public ResponseEntity<JobCategory> createCategory(
            @RequestBody JobCategory category) {

        return ResponseEntity.ok(
                jobCategoryService.createCategory(
                        category
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<JobCategory>> getAllCategories() {

        return ResponseEntity.ok(
                jobCategoryService.getAllCategories()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobCategory> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                jobCategoryService.getCategoryById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Long id) {

        jobCategoryService.deleteCategory(id);

        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }
}