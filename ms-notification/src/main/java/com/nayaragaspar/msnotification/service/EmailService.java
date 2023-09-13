package com.nayaragaspar.msnotification.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.nayaragaspar.msnotification.model.dto.SendEmailDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmailService {
    private final MailSender mailSender;

    public void sendAwsEmail(SendEmailDto email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("nayaradenisegaspar@gmail.com");
        simpleMailMessage.setTo(email.to());
        simpleMailMessage.setSubject(email.subject());
        simpleMailMessage.setText(email.body());

        this.mailSender.send(simpleMailMessage);
    }
}
