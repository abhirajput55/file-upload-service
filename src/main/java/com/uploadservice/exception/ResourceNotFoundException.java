package com.uploadservice.exception;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1567819862795115435L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
		super(String.format("%s not found with the given %s : %s", resourceName, fieldName, fieldValue));
	}

}
