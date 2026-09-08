package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminReportResponseDTO {

    private UserReport users;
    private JobReport jobs;
    private CompanyReport companies;
    private ApplicationReport applications;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserReport {

        private long total;
        private long candidates;
        private long recruiters;
        private long admins;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobReport {

        private long total;
        private long active;
        private long removed;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyReport {

        private long total;
        private long approved;
        private long pending;
        private long rejected;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicationReport {

        private long total;
        private long applied;
        private long shortlisted;
        private long rejected;
        private long hired;
    }
}