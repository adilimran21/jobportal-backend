package com.jobportal.backend.controller;

import com.jobportal.backend.dto.AdminReportResponseDTO;
import com.jobportal.backend.dto.AdminReportStatisticsDTO;
import com.jobportal.backend.dto.ApplicationResponseDTO;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.service.AdminApplicationService;
import com.jobportal.backend.service.AdminCompanyService;
import com.jobportal.backend.service.AdminJobService;
import com.jobportal.backend.service.AdminReportService;
import com.jobportal.backend.service.AdminReportStatisticsService;
import com.jobportal.backend.service.AdminStatisticsService;
import com.jobportal.backend.service.AdminUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdminStatisticsService adminStatisticsService;
    private final AdminUserService adminUserService;
    private final AdminJobService adminJobService;
    private final AdminCompanyService adminCompanyService;
    private final AdminApplicationService adminApplicationService;
    private final AdminReportService adminReportService;
    private final AdminReportStatisticsService adminReportStatisticsService;

    public AdminController(
            AdminStatisticsService adminStatisticsService,
            AdminUserService adminUserService,
            AdminJobService adminJobService,
            AdminCompanyService adminCompanyService,
            AdminApplicationService adminApplicationService,
            AdminReportService adminReportService,
            AdminReportStatisticsService adminReportStatisticsService) {

        this.adminStatisticsService = adminStatisticsService;
        this.adminUserService = adminUserService;
        this.adminJobService = adminJobService;
        this.adminCompanyService = adminCompanyService;
        this.adminApplicationService = adminApplicationService;
        this.adminReportService = adminReportService;
        this.adminReportStatisticsService = adminReportStatisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Long>> getDashboardStatistics() {

        return ResponseEntity.ok(
                adminStatisticsService.getDashboardStatistics());
    }

    @GetMapping("/reports")
    public ResponseEntity<AdminReportResponseDTO> getAdminReport() {

        return ResponseEntity.ok(
                adminReportService.generateReport());
    }

    @GetMapping("/reports/statistics")
    public ResponseEntity<AdminReportStatisticsDTO>
    getReportStatistics() {

        return ResponseEntity.ok(
                adminReportStatisticsService.generateStatistics());
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {

        return ResponseEntity.ok(
                adminUserService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserEntity> getUserById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminUserService.getUserById(id));
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<UserEntity> updateUserStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {

        String adminEmail = authentication.getName();

        return ResponseEntity.ok(
                adminUserService.updateUserStatus(
                        id,
                        status,
                        adminEmail));
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobEntity>> getAllJobsForAdmin() {

        return ResponseEntity.ok(
                adminJobService.getAllJobs());
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity<JobEntity> getJobDetailsForAdmin(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminJobService.getJobById(id));
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity<JobEntity> updateJob(
            @PathVariable Long id,
            @RequestBody JobEntity job) {

        return ResponseEntity.ok(
                adminJobService.updateJob(
                        id,
                        job));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<JobEntity> deleteJob(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminJobService.removeJob(id));
    }

    @GetMapping("/companies")
    public ResponseEntity<List<RecruiterProfile>> getAllCompanies() {

        return ResponseEntity.ok(
                adminCompanyService.getAllCompanies());
    }

    @GetMapping("/companies/{id}")
    public ResponseEntity<RecruiterProfile> getCompanyById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                adminCompanyService.getCompanyById(id));
    }

    @PutMapping("/companies/{id}/status")
    public ResponseEntity<RecruiterProfile> updateCompanyStatus(
            @PathVariable Long id,
            @RequestParam String status,
            Authentication authentication) {

        String adminEmail = authentication.getName();

        return ResponseEntity.ok(
                adminCompanyService.updateCompanyStatus(
                        id,
                        status,
                        adminEmail));
    }

    @PutMapping("/companies/{id}/moderation")
    public ResponseEntity<RecruiterProfile> updateModerationStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                adminCompanyService.updateModerationStatus(
                        id,
                        status));
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationResponseDTO>>
    getAllApplications() {

        return ResponseEntity.ok(
                adminApplicationService.getAllApplications());
    }

    @GetMapping("/applications/statistics")
    public ResponseEntity<Map<String, Long>>
    getApplicationStatistics() {

        return ResponseEntity.ok(
                adminApplicationService.getApplicationStatistics());
    }
}