package com.gtg.web.manager.resources;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class FailureResponseResource implements Serializable {

	private static final long serialVersionUID = 1L;
	List<String> errors;
	private String userMessage = "";
	private String developerMessage = "";
	private String statusCode = "";

	public FailureResponseResource() {
		super();
	}

	public FailureResponseResource(String userMessage, String developerMessage, String statusCode) {
		super();
		this.userMessage = userMessage;
		this.developerMessage = developerMessage;
		this.statusCode = statusCode;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

}
