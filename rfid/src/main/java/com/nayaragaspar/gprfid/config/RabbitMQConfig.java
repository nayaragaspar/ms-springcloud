package com.nayaragaspar.gprfid.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.reader-exception}")
    private String readerExceptionQueue;

    @Value("${broker.exchange.resource}")
    private String resourceExchange;

    @Value("${broker.queue.antena}")
    private String antenaQueue;

    public static final String EXCEPTION_ROUTING_KEY = "exception";
    public static final String CONNECT_ROUTING_KEY = "connect";

    // resource
    @Bean
    DirectExchange resourceExchange() {
        return new DirectExchange(resourceExchange);
    }

    @Bean
    FanoutExchange deadResourceExchange() {
        return new FanoutExchange(resourceExchange + ".dlx");
    }

    @Bean
    Queue antenaQueue() {
        return QueueBuilder.durable(antenaQueue)
                .deadLetterExchange(deadResourceExchange().getName())
                .build();
    }

    @Bean
    Queue antenaQueueDlq() {
        return new Queue(antenaQueue + ".dlq");
    }

    @Bean
    Binding antenaBinding() {
        return BindingBuilder.bind(antenaQueue())
                .to(resourceExchange())
                .with(CONNECT_ROUTING_KEY);
    }

    @Bean
    Binding deadAntenaBinding() {
        return BindingBuilder.bind(antenaQueueDlq())
                .to(deadResourceExchange());
    }

    // exception
    @Bean
    Queue readerExceptionQueue() {
        return new Queue(readerExceptionQueue, true);
    }

    @Bean
    Binding exceptionBinding() {
        return BindingBuilder.bind(readerExceptionQueue())
                .to(resourceExchange())
                .with(EXCEPTION_ROUTING_KEY);
    }

    // Config
    @Bean
    MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}