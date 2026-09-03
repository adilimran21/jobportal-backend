package com.jobportal.backend.service;

import com.jobportal.backend.dto.NotificationResponseDTO;
import com.jobportal.backend.entity.CandidateProfile;
import com.jobportal.backend.entity.NotificationEntity;
import com.jobportal.backend.repository.CandidateProfileRepository;
import com.jobportal.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final CandidateProfileRepository candidateProfileRepository;

        public NotificationService(
                        NotificationRepository notificationRepository,
                        CandidateProfileRepository candidateProfileRepository) {

                this.notificationRepository = notificationRepository;
                this.candidateProfileRepository = candidateProfileRepository;
        }

        public void createNotification(
                        String email,
                        String message,
                        String type) {

                CandidateProfile candidate = candidateProfileRepository
                                .findByUserEmail(email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Candidate profile not found"));

                NotificationEntity notification = new NotificationEntity();

                notification.setCandidate(candidate);
                notification.setMessage(message);
                notification.setType(type);
                notification.setRead(false);
                notification.setCreatedAt(LocalDateTime.now());

                notificationRepository.save(notification);
        }

        public List<NotificationResponseDTO> getCandidateNotifications(
                        String email) {

                return notificationRepository
                                .findByCandidateUserEmailOrderByCreatedAtDesc(email)
                                .stream()
                                .map(this::convertToDTO)
                                .toList();
        }

        public void markAsRead(
                        Long notificationId,
                        String email) {

                NotificationEntity notification = notificationRepository.findById(notificationId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Notification not found"));

                if (!notification.getCandidate()
                                .getUser()
                                .getEmail()
                                .equals(email)) {

                        throw new RuntimeException(
                                        "Access denied");
                }

                notification.setRead(true);

                notificationRepository.save(notification);
        }

        private NotificationResponseDTO convertToDTO(
                        NotificationEntity notification) {

                return new NotificationResponseDTO(
                                notification.getId(),
                                notification.getMessage(),
                                notification.getType(),
                                notification.isRead(),
                                notification.getCreatedAt());
        }
}