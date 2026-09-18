package com.jobportal.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileResponseDTO {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String location;

    private String profileImage;

    private String jobTitle;

    private String headline;

    private String careerStatus;

    private String experience;

    private String currentCompany;

    private String currentSalary;

    private String skills;

    private String tenthMarks;

    private String twelfthMarks;

    private String graduation;

    private String about;

    private String linkedin;

    private String portfolio;

    private String resume;
}