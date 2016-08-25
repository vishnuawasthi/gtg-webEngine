package com.gtg.web.manager.resources;

public class SuccessResponseResource {

	private Long id;
	private String statusCode = "";
	private String messagge = "";

	public SuccessResponseResource() {
	}

	public SuccessResponseResource(Long id, String statusCode, String messagge) {
		this.id = id;
		this.statusCode = statusCode;
		this.messagge = messagge;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessagge() {
		return messagge;
	}

	public void setMessagge(String messagge) {
		this.messagge = messagge;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
