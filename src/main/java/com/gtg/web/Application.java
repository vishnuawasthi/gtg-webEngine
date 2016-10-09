package com.gtg.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.gtg.services.jobs.JobRunner;
@EnableAsync
@EnableWebMvc
@EntityScan(basePackages="com.gtg.core.entity")
@EnableSpringDataWebSupport
@EnableAutoConfiguration
@EnableScheduling
@EnableJpaRepositories(basePackages="com.gtg.core.repository")
@PropertySources(value= {
	@PropertySource(value={"classpath:messages.properties"})
})
@ComponentScan(basePackages = {
		"com.gtg.web.controller",
		"com.gtg.web.security.service",
		"com.gtg.web.utils" ,
		"com.gtg.web.assembler",
		"com.gtg.web.assembler",
		"com.gtg.web.controller",
		"com.gtg.web.controller",
		"com.gtg.services.service",
		"com.gtg.web.config",
		"com.gtg.services.jobs",
		"com.gtg.email.service",
		"com.gtg.cache",
		"com.gtg.cache.config"
		
})
public class Application implements CommandLineRunner {

	@Autowired
	JobRunner jobRunner;
	
	@Override
	public void run(String... arg0){
		System.out.println("Starting  ....  gtg-web-Engine ");

		jobRunner.run();
		System.out.println("Started  ....  gtg-web-Engine ");
	}
	
	public static void main(String...strings ){
		ApplicationContext ctx = SpringApplication.run(Application.class, strings);
	}

}
