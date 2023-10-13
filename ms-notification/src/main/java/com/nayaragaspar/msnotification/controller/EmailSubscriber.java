package com.nayaragaspar.msnotification.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nayaragaspar.msnotification.model.dto.SendEmailDto;
import com.nayaragaspar.msnotification.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSubscriber {
    private final EmailService emailService;

    @RabbitListener(queues = "${mq.queues.enviar-email}")
    public void sendEmail(@Payload String payload) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SendEmailDto sendEmailDto = mapper.readValue(payload, SendEmailDto.class);

            emailService.sendAwsEmail(sendEmailDto);
        } catch (Exception e) {
            log.error("Erro ao receber solicitacao de envio de email: {} ", e.getMessage());
        }
    }
}
