package com.jobportal.backend.service;

import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "User not found"));
    }

    public UserEntity updateUserStatus(
            Long id,
            String status,
            String adminEmail) {

        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "User not found"));

        if (user.getEmail().equalsIgnoreCase(adminEmail)) {
            throw new RuntimeException(
                    "Admin cannot deactivate their own account");
        }

        if (!status.equalsIgnoreCase("ACTIVE") &&
                !status.equalsIgnoreCase("INACTIVE")) {

            throw new RuntimeException(
                    "Status must be ACTIVE or INACTIVE");
        }

        user.setStatus(status.toUpperCase());

        return userRepository.save(user);
    }
}