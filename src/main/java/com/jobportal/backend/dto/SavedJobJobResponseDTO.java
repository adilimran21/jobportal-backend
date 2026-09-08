package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedJobJobResponseDTO {

    private Long id;
    private String title;
    private String company;
    private String location;
    private String description;
    private String responsibilities;
    private String salary;
    private String jobType;
    private String skills;
    private String goodToHave;
    private String qualifications;
    private String experience;
    private String contractType;
    private String workMode;
    private Integer vacancies;
    private LocalDate applicationDeadline;
    private LocalDate postedDate;
    private String status;
}