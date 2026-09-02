package com.jobportal.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false, length = 3000)
    private String description;

    @Column(length = 5000)
    private String responsibilities;

    @Column(nullable = false)
    private String salary;

    @Column(nullable = false)
    private String jobType;

    @Column(length = 2000)
    private String skills;

    @Column(length = 2000)
    private String goodToHave;

    @Column(length = 2000)
    private String qualifications;

    private String experience;

    private String contractType;

    private String workMode;

    private Integer vacancies;

    private LocalDate applicationDeadline;

    private LocalDate postedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private JobCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    @JsonIgnore
    private RecruiterProfile recruiter;
}