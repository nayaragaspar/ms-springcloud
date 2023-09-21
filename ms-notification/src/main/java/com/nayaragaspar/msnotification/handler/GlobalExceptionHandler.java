package com.nayaragaspar.msnotification.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.nayaragaspar.msnotification.exception.CustomBadRequestException;
import com.nayaragaspar.msnotification.exception.CustomForbiddenException;
import com.nayaragaspar.msnotification.exception.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InternalError.class)
    public ResponseEntity<Object> handleExceptions(Exception exception, WebRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<Object> handleCustomBadRequest(CustomBadRequestException exception, WebRequest webRequest) {

        return ResponseEntity.badRequest().body(exception.getError());
    }

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<Object> handleCustomForbidden(CustomForbiddenException exception, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                exception.getMessage(),
                webRequest.getDescription(false));

        return ResponseEntity.badRequest().body(exceptionResponse);
    }
}