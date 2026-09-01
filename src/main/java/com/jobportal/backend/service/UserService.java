package com.jobportal.backend.service;

import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public UserService(
            UserRepository userRepository,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    
    // REGISTER
    
    public UserEntity createUser(UserEntity user) {

        // Check email
        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {

            throw new RuntimeException("Email is required");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        // Check password
        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Password is required"
            );
        }

        // Encrypt password
        String encodedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);

        // Save user
        return userRepository.save(user);
    }

    
    // LOGIN
    
    public String loginUser(String email, String password) {

        if (email == null || email.isBlank()) {
            return null;
        }

        if (password == null || password.isBlank()) {
            return null;
        }

        return userRepository.findByEmail(email)
                .filter(user ->
                        passwordEncoder.matches(
                                password,
                                user.getPassword()
                        )
                )
                .map(user ->
                        jwtService.generateToken(
                                user.getEmail(),
                                user.getRole()
                        )
                )
                .orElse(null);
    }

    
    // GET USER BY EMAIL
    
    public UserEntity getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElse(null);
    }
}