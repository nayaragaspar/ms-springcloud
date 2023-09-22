package com.nayaragaspar.msnotification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import com.nayaragaspar.msnotification.model.dto.SendEmailDto;

@Service
public class EmailService {
    @Autowired
    private MailSender mailSender;

    @Value("${mail.from}")
    private String from;

    public void sendAwsEmail(SendEmailDto email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(email.to());
        simpleMailMessage.setSubject(email.subject());
        simpleMailMessage.setText(email.body());

        this.mailSender.send(simpleMailMessage);
    }
}
