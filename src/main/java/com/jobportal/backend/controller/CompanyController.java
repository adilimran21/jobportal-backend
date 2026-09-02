package com.jobportal.backend.controller;

import com.jobportal.backend.dto.CompanyResponseDTO;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(
            CompanyService companyService) {

        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> getAllCompanies() {

        return ResponseEntity.ok(
                companyService.getAllCompanies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                companyService.getCompanyById(id)
        );
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<List<JobEntity>> getCompanyJobs(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                companyService.getCompanyJobs(id)
        );
    }
}