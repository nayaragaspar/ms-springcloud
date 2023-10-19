package com.nayaragaspar.msnotification.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.nayaragaspar.msnotification.model.dto.SendEmailDto;
import com.nayaragaspar.msnotification.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "${broker.queue.msnotification.enviar-email}")
    public void sendEmail(@Payload SendEmailDto payload) {
        try {
            /* ObjectMapper mapper = new ObjectMapper();
            SendEmailDto sendEmailDto = mapper.readValue(payload, SendEmailDto.class); */

            emailService.sendAwsEmail(payload);
        } catch (Exception e) {
            log.error("Erro ao receber solicitacao de envio de email: {} ", e.getMessage());
        }
    }
}
