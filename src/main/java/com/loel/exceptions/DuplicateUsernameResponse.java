package com.loel.exceptions;

public class DuplicateUsernameResponse {

	private String username;

	public DuplicateUsernameResponse(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
