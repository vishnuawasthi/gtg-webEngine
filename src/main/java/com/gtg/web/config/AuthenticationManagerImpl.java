package com.gtg.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.gtg.services.service.LoginService;

@Component("authenticationManager")
public class AuthenticationManagerImpl implements AuthenticationManager {

	//private SecurityDelegatingFilter filter;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		return authenticationProvider.authenticate(authentication);

	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(authenticationProvider);
		auth.userDetailsService(userDetailsService);
		//filter = new SecurityDelegatingFilter(loginService);
	}

}
