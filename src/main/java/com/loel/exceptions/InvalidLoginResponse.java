package com.loel.exceptions;

public class InvalidLoginResponse {
	private String username;
	private String password;

	public InvalidLoginResponse() {
		this.username = "You got something wrong pal";
		this.password = "Yep something is wrong bud";
	}

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