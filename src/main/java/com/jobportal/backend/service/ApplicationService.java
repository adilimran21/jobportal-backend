package com.jobportal.backend.service;

import com.jobportal.backend.dto.ApplicationResponseDTO;
import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.ApplicationStatus;
import com.jobportal.backend.entity.CandidateProfile;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.exception.DuplicateApplicationException;
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.CandidateProfileRepository;
import com.jobportal.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApplicationService {

        private final ApplicationRepository applicationRepository;
        private final CandidateProfileRepository candidateProfileRepository;
        private final JobRepository jobRepository;
        private final NotificationService notificationService;
        private final ApplicationStatusHistoryService historyService;
        private final EmailService emailService;

        public ApplicationService(
                        ApplicationRepository applicationRepository,
                        CandidateProfileRepository candidateProfileRepository,
                        JobRepository jobRepository,
                        NotificationService notificationService,
                        ApplicationStatusHistoryService historyService,
                        EmailService emailService) {

                this.applicationRepository = applicationRepository;
                this.candidateProfileRepository = candidateProfileRepository;
                this.jobRepository = jobRepository;
                this.notificationService = notificationService;
                this.historyService = historyService;
                this.emailService = emailService;
        }

        public ApplicationResponseDTO applyForJob(
                        String email,
                        Long jobId) {

                CandidateProfile candidate = candidateProfileRepository.findByUserEmail(email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Candidate profile not found"));

                JobEntity job = jobRepository.findById(jobId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Job not found"));

                boolean alreadyApplied = applicationRepository
                                .existsByCandidateUserEmailAndJobId(
                                                email,
                                                jobId);

                if (alreadyApplied) {
                        throw new DuplicateApplicationException(
                                        "You have already applied for this job");
                }

                Application application = new Application();

                application.setCandidate(candidate);
                application.setJob(job);
                application.setStatus(ApplicationStatus.APPLIED);
                application.setAppliedAt(LocalDateTime.now());

                Application savedApplication = applicationRepository.save(application);

                historyService.addHistory(
                                savedApplication,
                                ApplicationStatus.APPLIED);

                notificationService.createNotification(
                                email,
                                "You have successfully applied for "
                                                + job.getTitle()
                                                + " at "
                                                + job.getCompany()
                                                + ". Keep tracking your application for further updates.",
                                "APPLICATION_SUBMITTED");

                emailService.sendApplicationConfirmation(
                                email,
                                candidate.getUser().getName(),
                                job.getTitle(),
                                job.getCompany());

                return convertToResponseDTO(savedApplication);
        }

        public List<ApplicationResponseDTO> getCandidateApplications(
                        String email) {

                return applicationRepository
                                .findByCandidateUserEmail(email)
                                .stream()
                                .map(this::convertToResponseDTO)
                                .toList();
        }

        public List<ApplicationResponseDTO> getRecruiterApplications(
                        String email) {

                return applicationRepository
                                .findByJobRecruiterUserEmail(email)
                                .stream()
                                .map(this::convertToResponseDTO)
                                .toList();
        }

        public ApplicationResponseDTO getCandidateApplicationById(
                        Long applicationId,
                        String email) {

                Application application = applicationRepository
                                .findByIdAndCandidateUserEmail(
                                                applicationId,
                                                email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Application not found or access denied"));

                return convertToResponseDTO(application);
        }

        public ApplicationResponseDTO getRecruiterApplicationById(
                        Long applicationId,
                        String email) {

                Application application = applicationRepository
                                .findByIdAndJobRecruiterUserEmail(
                                                applicationId,
                                                email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Application not found or access denied"));

                return convertToResponseDTO(application);
        }

        public ApplicationResponseDTO getApplicationById(
                        Long applicationId) {

                Application application = applicationRepository.findById(applicationId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Application not found"));

                return convertToResponseDTO(application);
        }

        public ApplicationResponseDTO updateStatus(
                        Long applicationId,
                        ApplicationStatus status,
                        String recruiterEmail) {

                Application application = applicationRepository
                                .findByIdAndJobRecruiterUserEmail(
                                                applicationId,
                                                recruiterEmail)
                                .orElseThrow(() -> new RuntimeException(
                                                "Application not found or access denied"));

                ApplicationStatus oldStatus = application.getStatus();

                application.setStatus(status);

                Application updatedApplication = applicationRepository.save(application);

                if (oldStatus != status) {

                        historyService.addHistory(
                                        updatedApplication,
                                        status);

                        String candidateEmail = updatedApplication
                                        .getCandidate()
                                        .getUser()
                                        .getEmail();

                        String jobTitle = updatedApplication
                                        .getJob()
                                        .getTitle();

                        String company = updatedApplication
                                        .getJob()
                                        .getCompany();

                        String message;
                        String notificationType;

                        switch (status) {

                                case SHORTLISTED:

                                        message = "Great news! Your application for "
                                                        + jobTitle
                                                        + " at "
                                                        + company
                                                        + " has been shortlisted. Please keep tracking your application for further updates.";

                                        notificationType = "APPLICATION_SHORTLISTED";

                                        break;

                                case REJECTED:

                                        message = "Your application for "
                                                        + jobTitle
                                                        + " at "
                                                        + company
                                                        + " was not selected at this time. We encourage you to keep applying for other opportunities.";

                                        notificationType = "APPLICATION_REJECTED";

                                        break;

                                case HIRED:

                                        message = "Congratulations! We’re excited to let you know that your application for "
                                                        + jobTitle
                                                        + " at "
                                                        + company
                                                        + " has been successful. You have been selected for the position. "
                                                        + "Our team will contact you shortly regarding the further process.";

                                        notificationType = "APPLICATION_HIRED";

                                        break;

                                default:

                                        message = null;
                                        notificationType = null;
                        }

                        if (message != null) {

                                notificationService.createNotification(
                                                candidateEmail,
                                                message,
                                                notificationType);
                        }
                }

                return convertToResponseDTO(updatedApplication);
        }

        private ApplicationResponseDTO convertToResponseDTO(
                        Application application) {

                return new ApplicationResponseDTO(
                                application.getId(),
                                application.getJob().getId(),
                                application.getJob().getTitle(),
                                application.getJob().getCompany(),
                                application.getCandidate().getUser().getName(),
                                application.getCandidate().getUser().getEmail(),
                                application.getStatus().name(),
                                application.getAppliedAt());
        }
}