package com.gtg.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private SecurityDelegatingFilter filter;
	
    @Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;


	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	   	auth.userDetailsService(userDetailsService);
	   	auth.authenticationProvider(authenticationProvider);
	   	//filter = new SecurityDelegatingFilter(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers("/login", "/login*").permitAll()
        .antMatchers("*//api-docs/**").permitAll()
        .antMatchers("*//swagger-ui.html/*").permitAll()
        .antMatchers("*//*password**").permitAll()
        .antMatchers("/globalLogout", "/globalLogout*").permitAll()
       // .anyRequest().authenticated()
        .and()
        .csrf().disable();
		 
       // .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		 http.addFilterBefore(filter,UsernamePasswordAuthenticationFilter.class);
		 http.userDetailsService(userDetailsService());
	}
	
	@Override
	  protected UserDetailsService userDetailsService() {
	    return userDetailsService;
	  }
}
