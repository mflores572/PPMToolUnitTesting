package com.loel.payload;

import javax.validation.constraints.NotBlank;

public class LoginRequest {

	@NotBlank(message = "Gotta type something Sherlock")
	private String username;
	@NotBlank(message = "Blank is a little to easy to guess try typing a password")
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
}