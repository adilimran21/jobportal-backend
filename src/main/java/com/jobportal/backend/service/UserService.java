package com.jobportal.backend.service;

import com.jobportal.backend.entity.PasswordResetOTP;
import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.repository.PasswordResetOTPRepository;
import com.jobportal.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserService {

        private final UserRepository userRepository;
        private final JwtService jwtService;
        private final AdminNotificationService adminNotificationService;
        private final PasswordResetOTPRepository passwordResetOTPRepository;
        private final EmailService emailService;

        private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        public UserService(
                        UserRepository userRepository,
                        JwtService jwtService,
                        AdminNotificationService adminNotificationService,
                        PasswordResetOTPRepository passwordResetOTPRepository,
                        EmailService emailService) {

                this.userRepository = userRepository;
                this.jwtService = jwtService;
                this.adminNotificationService = adminNotificationService;
                this.passwordResetOTPRepository = passwordResetOTPRepository;
                this.emailService = emailService;
        }

        // =========================================================
        // REGISTER
        // =========================================================

        public UserEntity createUser(UserEntity user) {

                if (user.getEmail() == null ||
                                user.getEmail().isBlank()) {

                        throw new RuntimeException("Email is required");
                }

                if (userRepository.existsByEmail(user.getEmail())) {

                        throw new RuntimeException(
                                        "Email already registered");
                }

                if (user.getPassword() == null ||
                                user.getPassword().isBlank()) {

                        throw new RuntimeException(
                                        "Password is required");
                }

                if (user.getRole() == null ||
                                user.getRole().isBlank()) {

                        throw new RuntimeException("Role is required");
                }

                // ADMIN registration is not allowed
                if (user.getRole().equalsIgnoreCase("ADMIN")) {

                        throw new RuntimeException(
                                        "Admin registration is not allowed");
                }

                String encodedPassword = passwordEncoder.encode(user.getPassword());

                user.setPassword(encodedPassword);

                user.setStatus("ACTIVE");

                UserEntity savedUser = userRepository.save(user);

                if (user.getRole().equalsIgnoreCase("RECRUITER")) {

                        adminNotificationService.createNotification(
                                        "New recruiter registered: "
                                                        + user.getName()
                                                        + " (" + user.getEmail() + ")",
                                        "NEW_RECRUITER");
                }

                return savedUser;
        }

        // =========================================================
        // LOGIN
        // =========================================================

        public String loginUser(
                        String email,
                        String password) {

                if (email == null || email.isBlank()) {
                        return null;
                }

                if (password == null || password.isBlank()) {
                        return null;
                }

                return userRepository.findByEmail(email)

                                .filter(user -> user.getStatus() != null &&
                                                user.getStatus()
                                                                .equalsIgnoreCase("ACTIVE"))

                                .filter(user -> passwordEncoder.matches(
                                                password,
                                                user.getPassword()))

                                .map(user -> jwtService.generateToken(
                                                user.getEmail(),
                                                user.getRole()))

                                .orElse(null);
        }

        // =========================================================
        // GET USER BY EMAIL
        // =========================================================

        public UserEntity getUserByEmail(String email) {

                return userRepository.findByEmail(email)
                                .orElse(null);
        }

        // =========================================================
        // CHANGE PASSWORD
        // =========================================================

        public boolean changePassword(
                        String email,
                        String currentPassword,
                        String newPassword) {

                if (email == null ||
                                email.isBlank()) {

                        throw new RuntimeException(
                                        "User email is required");
                }

                if (currentPassword == null ||
                                currentPassword.isBlank()) {

                        throw new RuntimeException(
                                        "Current password is required");
                }

                if (newPassword == null ||
                                newPassword.isBlank()) {

                        throw new RuntimeException(
                                        "New password is required");
                }

                if (newPassword.length() < 6) {

                        throw new RuntimeException(
                                        "New password must contain at least 6 characters");
                }

                if (currentPassword.equals(newPassword)) {

                        throw new RuntimeException(
                                        "New password must be different from current password");
                }

                UserEntity user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException(
                                                "User not found"));

                // Verify current password
                if (!passwordEncoder.matches(
                                currentPassword,
                                user.getPassword())) {

                        throw new RuntimeException(
                                        "Current password is incorrect");
                }

                // Encode and save new password
                user.setPassword(
                                passwordEncoder.encode(newPassword));

                userRepository.save(user);

                return true;
        }

        // =========================================================
        // FORGOT PASSWORD
        // =========================================================

        public boolean sendPasswordResetOTP(
                        String email) {

                if (email == null ||
                                email.isBlank()) {

                        return false;
                }

                UserEntity user = userRepository.findByEmail(email)
                                .orElse(null);

                if (user == null) {
                        return false;
                }

                String otp = String.format(
                                "%06d",
                                new Random().nextInt(1000000));

                PasswordResetOTP passwordResetOTP = passwordResetOTPRepository
                                .findTopByEmailOrderByIdDesc(email)
                                .orElse(
                                                new PasswordResetOTP());

                passwordResetOTP.setEmail(email);

                passwordResetOTP.setOtp(otp);

                passwordResetOTP.setExpiresAt(
                                LocalDateTime.now()
                                                .plusMinutes(5));

                passwordResetOTP.setVerified(false);

                passwordResetOTPRepository.save(
                                passwordResetOTP);

                emailService.sendPasswordResetOTP(
                                email,
                                otp);

                return true;
        }

        // =========================================================
        // VERIFY OTP
        // =========================================================

        public boolean verifyPasswordResetOTP(
                        String email,
                        String otp) {

                PasswordResetOTP passwordResetOTP = passwordResetOTPRepository
                                .findTopByEmailOrderByIdDesc(email)
                                .orElse(null);

                if (passwordResetOTP == null) {
                        return false;
                }

                if (passwordResetOTP.isVerified()) {
                        return false;
                }

                if (passwordResetOTP.getExpiresAt()
                                .isBefore(LocalDateTime.now())) {

                        return false;
                }

                if (!passwordResetOTP.getOtp().equals(otp)) {
                        return false;
                }

                passwordResetOTP.setVerified(true);

                passwordResetOTPRepository.save(
                                passwordResetOTP);

                return true;
        }

        // =========================================================
        // RESET PASSWORD
        // =========================================================

        public boolean resetPassword(
                        String email,
                        String newPassword) {

                if (email == null ||
                                email.isBlank() ||
                                newPassword == null ||
                                newPassword.isBlank()) {

                        return false;
                }

                PasswordResetOTP passwordResetOTP = passwordResetOTPRepository
                                .findTopByEmailOrderByIdDesc(email)
                                .orElse(null);

                if (passwordResetOTP == null ||
                                !passwordResetOTP.isVerified()) {

                        return false;
                }

                if (passwordResetOTP.getExpiresAt()
                                .isBefore(LocalDateTime.now())) {

                        return false;
                }

                UserEntity user = userRepository.findByEmail(email)
                                .orElse(null);

                if (user == null) {
                        return false;
                }

                user.setPassword(
                                passwordEncoder.encode(
                                                newPassword));

                userRepository.save(user);

                passwordResetOTPRepository.delete(
                                passwordResetOTP);

                return true;
        }
}