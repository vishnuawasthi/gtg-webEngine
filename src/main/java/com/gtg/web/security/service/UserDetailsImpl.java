package com.gtg.web.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.gtg.core.entity.User;
import com.gtg.core.entity.UserRules;

@Component
public class UserDetailsImpl  extends User implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	public UserDetailsImpl(){}
	
	public UserDetailsImpl(User user){
		//this.setId(user.getId());
		//this.setFirstName(user.getFirstName());
		//this.setLastName(user.getLastName());
		//this.setEmail(user.getEmail());
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		//this.setUserRoles(user.getUserRules());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		System.out.println("getAuthorities()  -start");
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//Set<UserRules> userRules = this.getUserRoles();
		
		/*if (userRoles != null) {
			for (Roles roles : userRoles) {
				System.out.println("role   :  " + roles.getRoleName());
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roles.getRoleName());
				authorities.add(authority);
			}
		}*/
		
		UserRules userRole = this.getUserRules();
		if(null!= userRole){
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.getRuleName());
			authorities.add(authority);
		}
		
		System.out.println();
		System.out.println("Roles list       : "+authorities);
		System.out.println("getAuthorities()  -end");
		return authorities;
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
