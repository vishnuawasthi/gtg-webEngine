package com.gtg.web.manager.resources;

public class LoginResource {

	private String username;
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

	@Override
	public String toString() {
		return "LoginResource [username=" + username + ", password=" + password + "]";
	}
	
	
	

}
