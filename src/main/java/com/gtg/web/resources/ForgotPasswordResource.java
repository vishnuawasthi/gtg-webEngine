package com.gtg.web.resources;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

@SuppressWarnings("serial")
public class ForgotPasswordResource implements Serializable {

	@NotEmpty(message = "Username should not be empty")
	private String username;

	@NotEmpty(message = "Email should not be empty")
	private String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
