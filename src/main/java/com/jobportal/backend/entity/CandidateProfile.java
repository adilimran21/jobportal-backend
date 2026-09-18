package com.jobportal.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "candidate_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    private String phone;

    private String location;

    @Lob
    private String profileImage;

    private String jobTitle;

    private String headline;

    private String careerStatus;

    private String experience;

    private String currentCompany;

    private String currentSalary;

    @Column(length = 1000)
    private String skills;

    private String tenthMarks;

    private String twelfthMarks;

    private String graduation;

    @Column(length = 3000)
    private String about;

    private String linkedin;

    private String portfolio;

    private String resume;
}