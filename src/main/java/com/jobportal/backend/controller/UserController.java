package com.jobportal.backend.controller;

import com.jobportal.backend.entity.UserEntity;
import com.jobportal.backend.service.JwtService;
import com.jobportal.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

        private final UserService userService;
        private final JwtService jwtService;

        public UserController(
                        UserService userService,
                        JwtService jwtService) {

                this.userService = userService;
                this.jwtService = jwtService;
        }

        // REGISTER

        @PostMapping("/register")
        public ResponseEntity<?> registerUser(
                        @RequestBody UserEntity user) {

                try {

                        System.out.println("Name : " + user.getName());
                        System.out.println("Email : " + user.getEmail());
                        System.out.println("Role : " + user.getRole());

                        UserEntity savedUser = userService.createUser(user);

                        return ResponseEntity
                                        .ok(savedUser);

                } catch (RuntimeException e) {

                        return ResponseEntity
                                        .badRequest()
                                        .body(e.getMessage());
                }
        }

        // LOGIN

        @PostMapping("/login")
        public ResponseEntity<?> loginUser(
                        @RequestBody UserEntity user) {

                String token = userService.loginUser(
                                user.getEmail(),
                                user.getPassword());

                if (token != null) {

                        return ResponseEntity.ok(token);
                }

                return ResponseEntity
                                .status(401)
                                .body("Invalid email or password");
        }

        // FORGOT PASSWORD

        @PostMapping("/forgot-password")
        public ResponseEntity<?> forgotPassword(
                        @RequestBody Map<String, String> request) {

                String email = request.get("email");

                boolean accountFound = userService.sendPasswordResetOTP(email);

                if (accountFound) {

                        return ResponseEntity.ok(
                                        Map.of(
                                                        "message",
                                                        "Account found. OTP has been sent to your email."));
                }

                return ResponseEntity
                                .status(404)
                                .body(Map.of(
                                                "message",
                                                "Account not found"));
        }

        // VERIFY OTP

        @PostMapping("/verify-otp")
        public ResponseEntity<?> verifyOTP(
                        @RequestBody Map<String, String> request) {

                String email = request.get("email");
                String otp = request.get("otp");

                boolean verified = userService.verifyPasswordResetOTP(
                                email,
                                otp);

                if (verified) {

                        return ResponseEntity.ok(
                                        Map.of(
                                                        "message",
                                                        "OTP verified successfully"));
                }

                return ResponseEntity
                                .status(400)
                                .body(Map.of(
                                                "message",
                                                "Invalid or expired OTP"));
        }

        // RESET PASSWORD

        @PostMapping("/reset-password")
        public ResponseEntity<?> resetPassword(
                        @RequestBody Map<String, String> request) {

                String email = request.get("email");
                String newPassword = request.get("newPassword");

                boolean reset = userService.resetPassword(
                                email,
                                newPassword);

                if (reset) {

                        return ResponseEntity.ok(
                                        Map.of(
                                                        "message",
                                                        "Password reset successfully"));
                }

                return ResponseEntity
                                .status(400)
                                .body(Map.of(
                                                "message",
                                                "Password reset failed"));
        }

        // PROFILE

        @GetMapping("/profile")
        public ResponseEntity<?> getProfile(
                        @RequestHeader(value = "Authorization", required = false) String authHeader) {

                if (authHeader == null ||
                                !authHeader.startsWith("Bearer ")) {

                        return ResponseEntity
                                        .status(401)
                                        .body("Invalid Authorization header");
                }

                String token = authHeader.substring(7);

                try {

                        String email = jwtService.extractEmail(token);

                        UserEntity user = userService.getUserByEmail(email);

                        if (user != null) {

                                return ResponseEntity.ok(user);
                        }

                        return ResponseEntity
                                        .status(404)
                                        .body("User not found");

                } catch (Exception e) {

                        return ResponseEntity
                                        .status(401)
                                        .body("Invalid or expired token");
                }
        }
}