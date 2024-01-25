package com.nayaragaspar.gprfid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;
import lombok.Setter;;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
@Setter
public class CustomBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private Object error;

    public CustomBadRequestException(String msg) {
        super(msg);
    }

    public CustomBadRequestException(String msg, Object errorMessage) {
        super(msg);
        this.error = errorMessage;
    }

    public CustomBadRequestException(Object errorMessage) {
        super("");
        this.error = errorMessage;
    }
}
