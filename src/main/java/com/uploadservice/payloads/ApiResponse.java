package com.uploadservice.payloads;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


public class ApiResponse<T> {

	private boolean success;

	@JsonInclude(Include.NON_NULL)
	private String message;

	@JsonInclude(Include.NON_NULL)
	private T data;

	private ApiResponse(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	private ApiResponse(boolean success, String message) {
		this.success = success;
		this.message = message;

	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, null, data);
	}

	public static <T> ApiResponse<T> error(String message) {
		return new ApiResponse<>(false, message, null);
	}

	public static <T> ApiResponse<T> error(String message, T data) {
		return new ApiResponse<>(false, message, data);
	}

}
