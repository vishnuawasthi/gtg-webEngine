package com.gtg.web.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtg.lib.dto.LoginResponseDTO;
import com.gtg.lib.dto.UserDTO;
import com.gtg.services.service.LoginService;

@Component
public class SecurityDelegatingFilter extends DelegatingFilterProxy {

	@Autowired
	private LoginService loginService;

	public SecurityDelegatingFilter() {
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		System.out.println("doFilter() - start");

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String AUTHORIZATION = httpRequest.getHeader("Authority");
		String API_KEY =  httpRequest.getHeader("API_KEY");
		
		httpResponse.setHeader("Access-Control-Allow-Origin", "*");
		httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET,PUT, OPTIONS, DELETE");
		httpResponse.setHeader("Access-Control-Max-Age", "3600");
	    httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
	    httpResponse.setHeader("Access-Control-Allow-Headers", API_KEY + "," + AUTHORIZATION + ",content-type,x-requested-with");
		
	    
		StringBuffer requestUri = httpRequest.getRequestURL();
		String uriStr = new String(requestUri);
		String []  reqPaths = uriStr.split("/");
		
		System.out.println("size       : "+reqPaths.length);
		System.out.println("requestUri : "+reqPaths);
		
		//System.out.println(" 0= "+reqPaths[0]+" 1= "+reqPaths[1]+" 2= "+reqPaths[2]+"  3 "+reqPaths[3]+" 4= "+reqPaths[0]+" 5= "+reqPaths[5]);
		
		if(reqPaths.length >= 4  && "api".equalsIgnoreCase(reqPaths[3])) {
		
			ObjectMapper objectMapper = new ObjectMapper();
			
		if(!StringUtils.isEmpty(AUTHORIZATION) && !StringUtils.isEmpty( API_KEY)) {
			UserDTO dto = loginService.getByUsername(AUTHORIZATION);
			if(dto!=null  &&  validateKeys( API_KEY,dto)) {
				filterChain.doFilter(request, response);
			}else {
				LoginResponseDTO loginResponse = new LoginResponseDTO();
				loginResponse.setApiKey(null);
				loginResponse.setApikeyExpireTime(null);
				loginResponse.setDeveloperMessage("API Key has been expired, Please login again.");
				loginResponse.setUserMessage("API Key has been expired, Please login again.");
				loginResponse.setStatusCode(403);
				loginResponse.setStatus("FORBIDDEN");
			
				String  failedResponse = objectMapper.writeValueAsString(loginResponse);
				httpResponse.setStatus(403);
				httpResponse.setContentType("application/json");
				httpResponse.getWriter().write(failedResponse);
				//httpResponse.getOutputStream().flush();
				//httpResponse.getOutputStream().close();
			}
		}else {
		LoginResponseDTO loginResponse = new LoginResponseDTO();
		loginResponse.setApiKey(null);
		loginResponse.setApikeyExpireTime(null);
		loginResponse.setDeveloperMessage("Missing header(s) :  API_KEY,Authority.");
		loginResponse.setUserMessage("Missing header(s) :  API_KEY,Authority.");
		loginResponse.setStatusCode(400);
		loginResponse.setStatus("BAD REQUEST");
		//ObjectMapper objectMapper = new ObjectMapper();
		String  failedResponse = objectMapper.writeValueAsString(loginResponse);
		httpResponse.setStatus(400);
		httpResponse.setContentType("application/json");
		httpResponse.getWriter().write(failedResponse);
		//httpResponse.getOutputStream().flush();
	//	httpResponse.getOutputStream().close();
		System.out.println("doFilter() - end");
		}
		}else {
			System.out.println("doFilter() - Login Flow");
			filterChain.doFilter(request, response);
		}
	}
	
	private boolean validateKeys(String apiKey,UserDTO dto) {
		if(dto.getApiKey().equals(apiKey) &&  dto.getApikeyExpireTime().after(new Date()))
		return true;
		
		return false;
	}

}
