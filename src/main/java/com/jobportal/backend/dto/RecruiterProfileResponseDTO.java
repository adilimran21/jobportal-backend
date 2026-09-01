package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterProfileResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String companyName;

    private String companyLocation;

    private String companyDescription;

    private String website;
}