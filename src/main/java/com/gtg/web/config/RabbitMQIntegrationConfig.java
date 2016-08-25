package com.gtg.web.config;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gtg.processor.receiver.GTGEventReceiver;

@Configuration
public class RabbitMQIntegrationConfig {

	@Autowired
	AmqpAdmin rabbitAdmin;

	public static final String GTG_EXCHANGE_NAME = "gtg.exchange";

	public static final String CREATE_GTG_DATA_QUEUE = "gtg.queue";

	public static final String CREATE_GTG_DATA_ROUTING_KEY = "gtg.routingkey";

	@Bean(name = "createGTGDataQueue")
	public Queue getGTGDataQueue() {
		return new Queue(CREATE_GTG_DATA_QUEUE, true);
	}

	@Bean(name = "createGTGDataBinding")
	Binding getGDataBinding(Queue createGTGDataQueue, DirectExchange gtgDirectExchange) {
		return BindingBuilder.bind(createGTGDataQueue).to(gtgDirectExchange).with(CREATE_GTG_DATA_ROUTING_KEY);
	}

	@Bean(name = "gtgDirectExchange")
	public DirectExchange getGTGDirectExchange() {
		return new DirectExchange(GTG_EXCHANGE_NAME, true, false);
	}

	@PostConstruct
	public void initializeGTGMessageQueue() {
		DirectExchange gtgDirectExchange = getGTGDirectExchange();
		Queue getGTGDataQueue = getGTGDataQueue();
		Binding createGTGDataBinding = getGDataBinding(getGTGDataQueue, gtgDirectExchange);
		rabbitAdmin.declareBinding(createGTGDataBinding);
		rabbitAdmin.declareQueue();

	}

	@Bean
	SimpleMessageListenerContainer gtgMessageListenerContainer(ConnectionFactory connectionFactory,
			MessageListenerAdapter rabbitMQDataReceiver, MessageConverter jsonMessageConverter) {
		System.out.println("shopnowMessageListenerContainer()   -start");
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(CREATE_GTG_DATA_QUEUE);
		container.setMessageListener(rabbitMQDataReceiver);
		rabbitMQDataReceiver.setMessageConverter(jsonMessageConverter);
		System.out.println("shopnowMessageListenerContainer()   -end");
		return container;

	}

	@Bean
	public GTGEventReceiver GTGEventReceiver() {
		return new GTGEventReceiver();
	}

	@Bean
	MessageListenerAdapter listenerAdapter(GTGEventReceiver gtgEventReceiver) {
		return new MessageListenerAdapter(gtgEventReceiver, "receiveGTGEvent");
	}

}
