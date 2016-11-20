package com.gtg.services.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.gtg.lib.dto.LoginRequestDTO;
import com.gtg.lib.dto.LoginResponseDTO;
import com.gtg.services.exception.InvalidUserException;
import com.gtg.web.Application;

import junit.framework.Assert;

@TestPropertySource(value = "classpath:test-data.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class LoginServiceImplTest {

	@Value("${gtg.user.firstName}")
	private String fistName;

	@Value("${gtg.user.lastName}")
	private String lastName;

	@Value("${gtg.user.email}")
	private String email;

	@Value("${gtg.user.username}")
	private String username;
	
	@Value("${gtg.user.password")
	private String password;

	@Value("${gtg.login.status}")
	private String status;

	@Autowired
	private LoginService loginService;

	@SuppressWarnings("deprecation")
	@Test
	public void login() {
		LoginRequestDTO dto = new LoginRequestDTO();
		dto.setUsername(username);
		dto.setPassword(password);
		LoginResponseDTO loginResponseDTO = null;
		try {
			loginResponseDTO = loginService.login(dto);
			Assert.assertNotNull(loginResponseDTO);
			Assert.assertEquals(username, loginResponseDTO.getUsername());
			Assert.assertEquals(status, loginResponseDTO.getStatus());

		} catch (InvalidUserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
