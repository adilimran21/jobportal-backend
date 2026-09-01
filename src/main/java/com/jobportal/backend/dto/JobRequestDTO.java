package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequestDTO {

    private String title;

    private String company;

    private String description;

    private String jobType;

    private String location;

    private String salary;

    private String skills;
}