package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedJobResponseDTO {

    private Long id;
    private CandidateProfileResponseDTO candidate;
    private SavedJobJobResponseDTO job;
}