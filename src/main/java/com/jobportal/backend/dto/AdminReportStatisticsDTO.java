package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminReportStatisticsDTO {

    private Map<String, Long> jobsPostedByMonth;
    private Map<String, Long> applicationsByMonth;
}