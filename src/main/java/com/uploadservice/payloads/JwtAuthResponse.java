package com.uploadservice.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JwtAuthResponse {
	
	@JsonProperty("SUCCESS")
	private boolean status;
	
	@JsonProperty("TOKEN")
	private String token;
	
	

	public JwtAuthResponse(boolean status, String token) {
		super();
		this.status = status;
		this.token = token;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
}
