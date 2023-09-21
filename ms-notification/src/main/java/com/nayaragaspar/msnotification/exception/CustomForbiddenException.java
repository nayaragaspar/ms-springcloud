package com.nayaragaspar.msnotification.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
@Getter
@Setter
public class CustomForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CustomForbiddenException(String msg) {
        super(msg);
    }
}
