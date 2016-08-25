package com.gtg.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * This is implementation class  of AuthenticationProvider, Its need for spring security configuration
 * also @see org.springframework.security.authentication.AuthenticationProvider
 * 
 * @author Vishnu Awasthi
 *
 */


@Component(value = "authenticationProvider")
public class AuthenticationProviderImpl implements AuthenticationProvider {

	/*@Autowired
	private UserDetails userDetails;*/

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetails user = userDetailsService.loadUserByUsername(authentication.getName());

		if (name.equals(user.getUsername()) && password.equals(user.getPassword())) {
				final UserDetails principal = new User(name, password, user.getAuthorities());
				
				System.out.println(" user.getAuthorities()           ::::::::"+ user.getAuthorities());
				
				final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, user.getAuthorities());
				
				return auth;
		} else {
				return null;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		  return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
