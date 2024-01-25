package com.nayaragaspar.gprfid.producer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.nayaragaspar.gprfid.model.dto.Recurso;

@Component
public class RecursoProducer {
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    public static final String WRITE_ROUTING_KEY = "write";

    public RecursoProducer(RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    public Recurso getWriteResource(String ip) {
        return rabbitTemplate.convertSendAndReceiveAsType(
                directExchange.getName(),
                WRITE_ROUTING_KEY,
                ip,
                new ParameterizedTypeReference<>() {
                });
    }
}
