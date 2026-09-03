package com.jobportal.backend.dto;

import com.jobportal.backend.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryResponseDTO {

    private Long id;
    private ApplicationStatus status;
    private LocalDateTime changedAt;
}