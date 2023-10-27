package com.nayaragaspar.msacesso.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class VeiculoConsumer {
    @RabbitListener(queues = "${broker.queue.msveiculo.veiculo}")
    public void sendEmail(@Payload String payload) {
        log.info(payload);
    }
}
