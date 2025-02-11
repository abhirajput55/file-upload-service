package com.uploadservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.uploadservice.exception.InvalidFileException;
import com.uploadservice.service.FileUploadService;

@ExtendWith(MockitoExtension.class)
class FileUploadControllerTest {

    @Mock
    private FileUploadService fileUploadService;

    @InjectMocks
    private FileUploadController fileUploadController;

    private MockMultipartFile validFile;

    @BeforeEach
    void setup() {
        validFile = new MockMultipartFile(
                "file", "test.csv", "text/csv", "EmployeeID,ReviewDate,Goal,Achievement,Rating,Feedback".getBytes()
        );
    }

    @Test
    void uploadCsv_SuccessfulUpload_ReturnsOk() {
    	doNothing().when(fileUploadService).processCsvFile(any(MultipartFile.class));

        ResponseEntity<String> response = fileUploadController.uploadCsv(validFile);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("File uploaded and processed successfully.", response.getBody());

        verify(fileUploadService, times(1)).processCsvFile(any(MultipartFile.class));
    }

    @Test
    void uploadCsv_EmptyFile_ThrowsInvalidFileException() {
        MockMultipartFile emptyFile = new MockMultipartFile("file", "empty.csv", "text/csv", new byte[0]);

        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> {
            fileUploadController.uploadCsv(emptyFile);
        });

        assertEquals("Uploaded file is empty.", exception.getMessage());
    }

    @Test
    void uploadCsv_InvalidFileType_ThrowsInvalidFileException() {
        MockMultipartFile invalidFile = new MockMultipartFile("file", "test.txt", "text/plain", "Invalid Content".getBytes());

        InvalidFileException exception = assertThrows(InvalidFileException.class, () -> {
            fileUploadController.uploadCsv(invalidFile);
        });

        assertEquals("Only CSV files are allowed.", exception.getMessage());
    }

    @Test
    void uploadCsv_ExceedsFileSizeLimit_ThrowsInvalidFileException() {
        byte[] largeFileContent = new byte[6 * 1024 * 1024]; // 6MB file
        MockMultipartFile largeFile = new MockMultipartFile("file", "large.csv", "text/csv", largeFileContent);

        MaxUploadSizeExceededException exception = assertThrows(MaxUploadSizeExceededException.class, () -> {
            fileUploadController.uploadCsv(largeFile);
        });

        assertEquals("Maximum upload size of 5242880 bytes exceeded", exception.getMessage());
    }
}
