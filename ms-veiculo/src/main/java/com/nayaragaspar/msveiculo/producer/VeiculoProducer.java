package com.nayaragaspar.msveiculo.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nayaragaspar.msveiculo.model.Veiculo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class VeiculoProducer {
    final RabbitTemplate rabbitTemplate;

    @Value(value = "${broker.queue.msveiculo.veiculo}")
    private String veiculoQueue;

    @Value(value = "${broker.exchange.msveiculo.veiculo}")
    private String fanoutExchange;

    public void publishNovoVeiculoMessage(Veiculo veiculo) {
        rabbitTemplate.convertAndSend(fanoutExchange, veiculoQueue, veiculo);
    }
}
