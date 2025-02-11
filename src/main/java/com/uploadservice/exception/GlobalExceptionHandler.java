package com.uploadservice.exception;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.uploadservice.payloads.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Value("${spring.servlet.multipart.max-file-size}")
	private String maxUploadFileSize;
	
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        logger.error("Internal server error: ", ex);
        return new ResponseEntity<>(ApiResponse.error("Internal server error: " + ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("Handle ResourceNotFoundException api error: {}", ex);
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ApiResponse<String>> handleInvalidFileException(InvalidFileException ex) {
    	logger.error("Handle InvalidFileException error: {}", ex);
    	return new ResponseEntity<>(ApiResponse.error("Invalid File: " + ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValid(MethodArgumentNotValidException exc) {
		List<String> errors = new ArrayList<>();
		exc.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			errors.add(fieldName + ": " + error.getDefaultMessage());
		});
		logger.error("Handle MethodArgumentNotValid api error: {}", exc.getLocalizedMessage());
		return new ResponseEntity<>(ApiResponse.error("Error found: ", errors), HttpStatus.BAD_REQUEST);
	}

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<String>> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        logger.warn("File size exceeds the maximum allowed limit.");
        return new ResponseEntity<>(ApiResponse.error("File size exceeds the maximum allowed limit of " + maxUploadFileSize), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException exc) {
    	logger.error("Handle AccessDenied api error handler: {}", exc.getMessage());
		return new ResponseEntity<>(ApiResponse.error("You are not authorize to this request: " + exc.getMessage()), HttpStatus.UNAUTHORIZED);
	}
}