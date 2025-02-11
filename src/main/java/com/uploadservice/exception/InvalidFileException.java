package com.uploadservice.exception;

public class InvalidFileException extends RuntimeException {
    
	private static final long serialVersionUID = 4061417505167786821L;

	public InvalidFileException(String message) {
        super(message);
    }
}