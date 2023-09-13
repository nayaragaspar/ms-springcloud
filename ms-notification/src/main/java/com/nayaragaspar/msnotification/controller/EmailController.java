package com.nayaragaspar.msnotification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nayaragaspar.msnotification.model.dto.SendEmailDto;
import com.nayaragaspar.msnotification.service.EmailService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<String> sendAwsEmail(@Valid @RequestBody SendEmailDto email) {
        emailService.sendAwsEmail(email);

        return ResponseEntity.ok("Email eviado com sucesso!");
    }
}
