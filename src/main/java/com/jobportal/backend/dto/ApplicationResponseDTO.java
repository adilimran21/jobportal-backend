package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponseDTO {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String company;
    private String candidateName;
    private String candidateEmail;
    private String status;
    private LocalDateTime appliedAt;
}