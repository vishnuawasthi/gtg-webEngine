package com.gtg.web.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gtg.core.entity.User;
import com.gtg.core.repository.UserRepository;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UserDetailsServiceImpl : loadUserByUsername() -start");
		
		System.out.println("username  : "+username);
		User user = userRepository.findUserByUsername(username);
		
		System.out.println("user   : "+user);
		
		
		UserDetails userDetails = new UserDetailsImpl(user);
		System.out.println("userDetails   :::::::::"+userDetails.getUsername());
		
		System.out.println("userDetails   :::::::::"+userDetails.getPassword());
		
		System.out.println("userDetails   :::::::::"+userDetails.getAuthorities());
		
		
		System.out.println("UserDetailsServiceImpl : loadUserByUsername() -end");
		return userDetails;
	}

}
