package com.uploadservice.payloads;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JwtAuthRequest {
	
	@NotNull(message ="Username should not be null !!")
	@NotEmpty(message ="Username should not be empty !!")
	private String username;
	
	@NotNull(message ="Password should not be null !!")
	@NotEmpty(message ="Password should not be empty !!")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JwtAuthRequest(
			@NotNull(message = "Username should not be null !!") @NotEmpty(message = "Username should not be empty !!") String username,
			@NotNull(message = "Password should not be null !!") @NotEmpty(message = "Password should not be empty !!") String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	

}