package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendationResponseDTO {

    private Long jobId;
    private String title;
    private String company;
    private int matchScore;
}