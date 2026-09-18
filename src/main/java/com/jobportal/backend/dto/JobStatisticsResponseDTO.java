package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatisticsResponseDTO {

    // Total jobs ever posted by recruiter
    private long totalJobs;

    // Currently active jobs
    private long activeJobs;

    // Jobs removed by recruiter
    private long removedJobs;

    // Application statistics
    private long totalApplications;

    private long pendingApplications;

    private long shortlistedApplications;

    private long rejectedApplications;

    private long hiredApplications;
}