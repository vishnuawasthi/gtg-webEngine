package com.gtg.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * 
 * @author Vishnu Awasthi
 *         <h3>Swagger Configuration</h3>
 *         <p>
 *         When creating a REST API, good documentation is instrumental.
 * 
 *         Moreover, every change in the API should be simultaneously described
 *         in the reference documentation. Accomplishing this manually is a
 *         tedious exercise, so automation of the process was inevitable.
 *         </p>
 */

@Configuration
public class SwaggerConfiguration {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.gtg.web.manager.controller"))
				.paths(PathSelectors.any())
				//.paths(PathSelectors.ant("/*"))
				// .paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo())
				.useDefaultResponseMessages(false);
	}

	private ApiInfo apiInfo() {
		
		Contact contact = new Contact(
				"GTG SERVICE PORTAL",
				"https://www.linkedin.com/in/vishnu-awasthi-36099b77?trk=nav_responsive_tab_profile",
				"vishnuawasthi121@gmail.com");
		ApiInfo apiInfo = new ApiInfo(
				"GTG SERVICE PORTAL", 
				"This all api belongs to operation supported by GTG Portal",
				"1.0", 
				"", 
				contact, 
				"GTG SERVICE  LICENCE", 
				"");
		return apiInfo;
	}

}
