package com.nayaragaspar.msacesso.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.queue.msveiculo.veiculo}")
    private String veiculoQueue;

    @Value("${broker.exchange.msveiculo.veiculo}")
    private String fanoutExchange;

    @Bean
    FanoutExchange fanout() {
        return new FanoutExchange(fanoutExchange);
    }

    @Bean
    Queue queue() {
        return new Queue(veiculoQueue, true);
    }

    @Bean
    public Binding binding(FanoutExchange fanout,
            Queue queue) {
        return BindingBuilder.bind(queue).to(fanout);
    }

    /* @Bean
    Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    } */

}