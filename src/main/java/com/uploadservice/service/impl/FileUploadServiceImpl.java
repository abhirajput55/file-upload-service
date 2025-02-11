package com.uploadservice.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uploadservice.entity.EmployeeReview;
import com.uploadservice.exception.InvalidFileException;
import com.uploadservice.repository.EmployeeReviewRepository;
import com.uploadservice.service.FileUploadService;
import com.uploadservice.utils.CsvFileUtility;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileUploadServiceImpl implements FileUploadService {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);
    private final EmployeeReviewRepository employeeReviewRepository;
    private static final int BATCH_SIZE = 100;
    
    public FileUploadServiceImpl(EmployeeReviewRepository employeeReviewRepository) {
        this.employeeReviewRepository = employeeReviewRepository;
    }
    
    public void processCsvFile(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            logger.info("Processing CSV file: {}", file.getOriginalFilename());
            List<EmployeeReview> batch = new ArrayList<>();
            reader.lines().skip(1).forEach(line -> {
                EmployeeReview record = parseCsvLine(line);
                if (!employeeReviewRepository.existsByEmployeeIdAndReviewDate(record.getEmployeeId(), record.getReviewDate())) {
                    batch.add(record);
                }
                if (batch.size() >= BATCH_SIZE) {
                    logger.info("Saving batch of {} records to database.", batch.size());
                    employeeReviewRepository.saveAll(batch);
                    batch.clear();
                }
            });
            if (!batch.isEmpty()) {
                logger.info("Saving final batch of {} records to database.", batch.size());
                employeeReviewRepository.saveAll(batch);
            }
            logger.info("CSV file processing completed successfully.");
        } catch (Exception e) {
            logger.error("Error processing CSV file: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("Error processing CSV file", e);
        }
    }
    
    private EmployeeReview parseCsvLine(String line) {
        String[] data = line.split(",");

        if (data.length != 6) {
            throw new InvalidFileException("Invalid CSV format: Each row must have exactly 6 values.");
        }

        try {
            Long employeeId = Long.parseLong(data[0].trim());

            if (!CsvFileUtility.isValidDateFormat(data[1].trim())) {
                throw new InvalidFileException("Invalid date format for reviewDate: " + data[1].trim());
            }
            
            String reviewDate = CsvFileUtility.convertToUTC(data[1].trim());

            String goal = CsvFileUtility.validateStringField(data[2], "Goal");
            String achievement = CsvFileUtility.validateStringField(data[3], "Achievement");
            int rating = Integer.parseInt(data[4].trim());

            if (rating < 1 || rating > 5) {
                throw new InvalidFileException("Rating must be between 1 and 5.");
            }

            String feedback = CsvFileUtility.validateStringField(data[5], "Feedback");

            return new EmployeeReview(employeeId, reviewDate, goal, achievement, rating, feedback);
        } catch (Exception e) {
            throw new InvalidFileException(e.getMessage());
        }
    }

}
