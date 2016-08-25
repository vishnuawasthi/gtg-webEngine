package com.gtg.web.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfiguration {

	@Bean
	public ConnectionFactory connectionFactory() {
		System.out.println("onnectionFactory() -start ");
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost",5672);
														connectionFactory.setUsername("guest");
														connectionFactory.setPassword("guest");
														//connectionFactory.setConnectionTimeout(20000);
		System.out.println("onnectionFactory() -end ");
														return connectionFactory;
													
		}

	@Bean
	@Primary
	public AmqpAdmin getRabbitAdmin(ConnectionFactory connectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
		
		
		return rabbitAdmin;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate =new RabbitTemplate(connectionFactory());
										rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter() );
										return rabbitTemplate; 
	}

	@Bean(name="jackson2JsonMessageConverter")
	@Primary
	public MessageConverter jackson2JsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
	
	
}
