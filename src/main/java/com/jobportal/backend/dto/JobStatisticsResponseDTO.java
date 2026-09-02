package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatisticsResponseDTO {

    private long totalJobs;

    private long totalApplications;

    private long pendingApplications;

    private long shortlistedApplications;

    private long rejectedApplications;

    private long hiredApplications;
}