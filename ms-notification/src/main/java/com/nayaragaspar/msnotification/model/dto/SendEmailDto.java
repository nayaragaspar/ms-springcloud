package com.nayaragaspar.msnotification.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SendEmailDto(@Email String to, @NotEmpty String subject, @NotEmpty String body) {

}
