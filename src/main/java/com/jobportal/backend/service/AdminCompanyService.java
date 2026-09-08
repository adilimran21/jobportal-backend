package com.jobportal.backend.service;

import com.jobportal.backend.entity.RecruiterProfile;
import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.RecruiterProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminCompanyService {

        private final RecruiterProfileRepository recruiterProfileRepository;

        public AdminCompanyService(
                        RecruiterProfileRepository recruiterProfileRepository) {

                this.recruiterProfileRepository = recruiterProfileRepository;
        }

        public List<RecruiterProfile> getAllCompanies() {

                return recruiterProfileRepository.findAll();
        }

        public RecruiterProfile getCompanyById(Long id) {

                return recruiterProfileRepository
                                .findById(id)
                                .orElseThrow(() -> new RuntimeException("Company not found"));
        }

        public RecruiterProfile updateCompanyStatus(
                        Long id,
                        String status,
                        String adminEmail) {

                RecruiterProfile company = recruiterProfileRepository
                                .findById(id)
                                .orElseThrow(() -> new RuntimeException("Company not found"));

                UserEntity user = company.getUser();

                if (user.getEmail().equalsIgnoreCase(adminEmail)) {
                        throw new RuntimeException(
                                        "Admin cannot deactivate their own account");
                }

                if (!status.equalsIgnoreCase("ACTIVE") &&
                                !status.equalsIgnoreCase("INACTIVE")) {

                        throw new IllegalArgumentException(
                                        "Status must be ACTIVE or INACTIVE");
                }

                user.setStatus(status.toUpperCase());

                return recruiterProfileRepository.save(company);
        }

        public RecruiterProfile updateModerationStatus(
                        Long id,
                        String moderationStatus) {

                RecruiterProfile company = recruiterProfileRepository
                                .findById(id)
                                .orElseThrow(() -> new RuntimeException("Company not found"));

                if (!moderationStatus.equalsIgnoreCase("PENDING") &&
                                !moderationStatus.equalsIgnoreCase("APPROVED") &&
                                !moderationStatus.equalsIgnoreCase("REJECTED")) {

                        throw new IllegalArgumentException(
                                        "Moderation status must be PENDING, APPROVED or REJECTED");
                }

                company.setModerationStatus(
                                moderationStatus.toUpperCase());

                return recruiterProfileRepository.save(company);
        }
}