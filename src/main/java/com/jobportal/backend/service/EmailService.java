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
        message.setSubject("Application Submitted Successfully - " + jobTitle);

        message.setText(
                "Hello " + candidateName + ",\n\n" +
                "Your application has been successfully submitted.\n\n" +
                "Job: " + jobTitle + "\n" +
                "Company: " + company + "\n\n" +
                "You can track your application status through your Job Portal account.\n\n" +
                "Best regards,\n" +
                "Job Portal Team"
        );

        mailSender.send(message);
    }
}