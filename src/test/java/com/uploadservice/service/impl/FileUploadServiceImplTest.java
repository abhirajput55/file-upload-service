package com.uploadservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.uploadservice.exception.InvalidFileException;
import com.uploadservice.repository.EmployeeReviewRepository;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceImplTest {

    @Mock
    private EmployeeReviewRepository employeeReviewRepository;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private FileUploadServiceImpl fileUploadService;

    private String validCsvContent;
    private String invalidCsvContent;

    @BeforeEach
    void setUp() {
        validCsvContent = "1,2023-12-01,Goal1,Achievement1,5,Feedback1\n" +
                          "2,2023-12-02,Goal2,Achievement2,4,Feedback2";

        invalidCsvContent = "1,invalid-date,Goal1,Achievement1,5,Feedback1"; // Invalid date and rating
    }

    @Test
    void processCsvFile_ValidData_SavesToDatabase() throws Exception {
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(validCsvContent.getBytes(StandardCharsets.UTF_8)));
        when(employeeReviewRepository.existsByEmployeeIdAndReviewDate(anyLong(), any())).thenReturn(false);
        when(employeeReviewRepository.saveAll(any(List.class))).thenReturn(null);

        assertDoesNotThrow(() -> fileUploadService.processCsvFile(multipartFile));

        verify(employeeReviewRepository, atLeastOnce()).saveAll(any(List.class));
    }

    @Test
    void processCsvFile_ExceptionWhileReadingFile_ThrowsRuntimeException() throws Exception {
        when(multipartFile.getInputStream()).thenThrow(new RuntimeException("File read error"));
        
        Exception exception = assertThrows(RuntimeException.class, () -> fileUploadService.processCsvFile(multipartFile));
        assertEquals("Error processing CSV file", exception.getMessage());
    }
}
