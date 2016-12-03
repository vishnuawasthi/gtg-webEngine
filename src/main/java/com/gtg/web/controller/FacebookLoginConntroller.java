package com.gtg.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gtg.lib.dto.LoginResponseDTO;
import com.gtg.web.resources.LoginResource;

public class FacebookLoginConntroller {

	@RequestMapping(value = "/facebook/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public HttpEntity<Object> login(@RequestBody LoginResource loginRequest, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("facebook() - start");
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();

		System.out.println("facebook() - end");
		return new ResponseEntity<Object>(loginResponseDTO, HttpStatus.OK);
	}
}
