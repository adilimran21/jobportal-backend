package com.jobportal.backend.repository;

import com.jobportal.backend.entity.CandidateProfile;
import com.jobportal.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CandidateProfileRepository
        extends JpaRepository<CandidateProfile, Long> {

    Optional<CandidateProfile> findByUser(UserEntity user);
}