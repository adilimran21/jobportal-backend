package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;
    private String message;
    private String type;
    private boolean read;
    private LocalDateTime createdAt;
}