package com.jobportal.backend.service;

import com.jobportal.backend.dto.AdminReportStatisticsDTO;
import com.jobportal.backend.entity.Application;
import com.jobportal.backend.entity.JobEntity;
import com.jobportal.backend.repository.ApplicationRepository;
import com.jobportal.backend.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdminReportStatisticsService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    private static final DateTimeFormatter MONTH_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM");

    public AdminReportStatisticsService(
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

        this.jobRepository = jobRepository;
        this.applicationRepository = applicationRepository;
    }

    public AdminReportStatisticsDTO generateStatistics() {

        Map<String, Long> jobsPostedByMonth =
                jobRepository.findAll()
                        .stream()
                        .map(JobEntity::getPostedDate)
                        .filter(date -> date != null)
                        .map(YearMonth::from)
                        .map(month -> month.format(MONTH_FORMAT))
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                TreeMap::new,
                                Collectors.counting()
                        ));

        Map<String, Long> applicationsByMonth =
                applicationRepository.findAll()
                        .stream()
                        .map(Application::getAppliedAt)
                        .filter(date -> date != null)
                        .map(YearMonth::from)
                        .map(month -> month.format(MONTH_FORMAT))
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                TreeMap::new,
                                Collectors.counting()
                        ));

        return new AdminReportStatisticsDTO(
                jobsPostedByMonth,
                applicationsByMonth
        );
    }
}