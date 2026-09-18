package com.jobportal.backend.config;

import com.jobportal.backend.entity.JobCategory;
import com.jobportal.backend.repository.JobCategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initializeJobCategories(
            JobCategoryRepository jobCategoryRepository) {

        return args -> {

            addCategory(
                    jobCategoryRepository,
                    "Software Development",
                    "Jobs related to software development and application engineering."
            );

            addCategory(
                    jobCategoryRepository,
                    "Frontend Development",
                    "Jobs related to frontend web development and user interfaces."
            );

            addCategory(
                    jobCategoryRepository,
                    "Backend Development",
                    "Jobs related to backend development, APIs and server-side applications."
            );

            addCategory(
                    jobCategoryRepository,
                    "Full Stack Development",
                    "Jobs involving both frontend and backend application development."
            );

            addCategory(
                    jobCategoryRepository,
                    "Data Science & Analytics",
                    "Jobs related to data analysis, data science and business intelligence."
            );

            addCategory(
                    jobCategoryRepository,
                    "DevOps & Cloud",
                    "Jobs related to DevOps, cloud computing and infrastructure."
            );

            addCategory(
                    jobCategoryRepository,
                    "Mobile Development",
                    "Jobs related to Android, iOS and mobile application development."
            );

            addCategory(
                    jobCategoryRepository,
                    "Cyber Security",
                    "Jobs related to cybersecurity, information security and application security."
            );

            addCategory(
                    jobCategoryRepository,
                    "UI/UX Design",
                    "Jobs related to user interface and user experience design."
            );

            addCategory(
                    jobCategoryRepository,
                    "QA & Testing",
                    "Jobs related to software quality assurance and testing."
            );

            addCategory(
                    jobCategoryRepository,
                    "Database & DBA",
                    "Jobs related to database development, administration and management."
            );

            addCategory(
                    jobCategoryRepository,
                    "IT Support",
                    "Jobs related to technical support, IT operations and helpdesk services."
            );
        };
    }


    private void addCategory(
            JobCategoryRepository jobCategoryRepository,
            String name,
            String description) {

        if (!jobCategoryRepository.existsByName(name)) {

            JobCategory category = new JobCategory();

            category.setName(name);
            category.setDescription(description);

            jobCategoryRepository.save(category);
        }
    }
}