package com.jobportal.backend.service;

import com.jobportal.backend.dto.CompanyResponseDTO;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.repository.JobRepository;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final RecruiterProfileRepository recruiterProfileRepository;
    private final JobRepository jobRepository;

    public CompanyService(
            RecruiterProfileRepository recruiterProfileRepository,
            JobRepository jobRepository) {

        this.recruiterProfileRepository = recruiterProfileRepository;

        this.jobRepository = jobRepository;
    }

    public List<CompanyResponseDTO> getAllCompanies() {

        return recruiterProfileRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public CompanyResponseDTO getCompanyById(
            Long companyId) {

        RecruiterProfile company = recruiterProfileRepository
                .findById(companyId)
                .orElseThrow(() -> new RuntimeException(
                        "Company not found"));

        return convertToDTO(company);
    }

    public List<JobEntity> getCompanyJobs(
            Long companyId) {

        recruiterProfileRepository
                .findById(companyId)
                .orElseThrow(() -> new RuntimeException(
                        "Company not found"));

        return jobRepository
                .findByRecruiterId(companyId);
    }

    private CompanyResponseDTO convertToDTO(
            RecruiterProfile company) {

        return new CompanyResponseDTO(
                company.getId(),
                company.getCompanyName(),
                company.getCompanyLocation(),
                company.getCompanyDescription(),
                company.getWebsite());
    }
}