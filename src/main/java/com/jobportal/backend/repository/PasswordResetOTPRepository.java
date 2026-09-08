package com.jobportal.backend.repository;

import com.jobportal.backend.entity.PasswordResetOTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetOTPRepository
        extends JpaRepository<PasswordResetOTP, Long> {

    Optional<PasswordResetOTP> findTopByEmailOrderByIdDesc(String email);
}