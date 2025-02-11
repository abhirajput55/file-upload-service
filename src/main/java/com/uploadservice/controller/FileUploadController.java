package com.uploadservice.controller;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.uploadservice.exception.InvalidFileException;
import com.uploadservice.service.FileUploadService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/upload")
public class FileUploadController {
	
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") @NotNull MultipartFile file) {
    	
    	if (file.isEmpty()) {
            throw new InvalidFileException("Uploaded file is empty.");
        }
        if (!file.getOriginalFilename().endsWith(".csv")) {
            throw new InvalidFileException("Only CSV files are allowed.");
        }
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB limit
            throw new MaxUploadSizeExceededException(10 * 1024 * 1024);
        }
        
        logger.info("Received CSV file upload request: {}", file.getOriginalFilename());
        fileUploadService.processCsvFile(file);
        logger.info("File processing completed successfully.");
        return ResponseEntity.ok("File uploaded and processed successfully.");
    }
}