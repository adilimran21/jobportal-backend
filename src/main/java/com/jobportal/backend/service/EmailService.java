package com.jobportal.backend.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApplicationConfirmation(
            String candidateEmail,
            String candidateName,
            String jobTitle,
            String company) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(candidateEmail);
        message.setSubject(
                "Application Submitted Successfully - " + jobTitle);

        message.setText(
                "Hello " + candidateName + ",\n\n" +
                        "Your application has been successfully submitted.\n\n" +
                        "Job: " + jobTitle + "\n" +
                        "Company: " + company + "\n\n" +
                        "You can track your application status through your Job Portal account.\n\n" +
                        "Best regards,\n" +
                        "Job Portal Team");

        mailSender.send(message);
    }

    public void sendPasswordResetOTP(
            String email,
            String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject(
                "Job Portal - Password Reset OTP");

        message.setText(
                "Hello,\n\n" +
                        "We received a request to reset your Job Portal password.\n\n" +
                        "Your OTP is: " + otp + "\n\n" +
                        "This OTP is valid for 5 minutes.\n\n" +
                        "If you did not request a password reset, " +
                        "please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "Job Portal Team");

        mailSender.send(message);
    }
}