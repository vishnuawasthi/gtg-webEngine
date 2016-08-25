package com.gtg.web.manager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.gtg.web.manager.resources.FailureResponseResource;
import com.gtg.web.manager.resources.SuccessResponseResource;

public class BaseController {

	public SuccessResponseResource getSuccessResponseResource(Long id, String statusCode, String messagge) {
		SuccessResponseResource resource = new SuccessResponseResource(id, statusCode, messagge);
		return resource;
	}

	public FailureResponseResource getFailureResponseResource(String statusCode, String developerMessage,
			String userMessage) {
		FailureResponseResource resource = new FailureResponseResource();
		resource.setStatusCode(statusCode);
		resource.setDeveloperMessage(developerMessage);
		resource.setUserMessage(userMessage);
		return resource;
	}

	public FailureResponseResource getValidationMessages(BindingResult result) {
		FailureResponseResource resource = new FailureResponseResource();
		List<String> errors = new ArrayList<String>(1);
		for (ObjectError obj : result.getAllErrors()) {

			errors.add(obj.getDefaultMessage());
		}
		resource.setErrors(errors);
		resource.setStatusCode("400");
		resource.setUserMessage("Invalid request, Please provide valid request format.");
		resource.setDeveloperMessage("Invalid request, Please provide valid request format.");
		return resource;
	}
}
