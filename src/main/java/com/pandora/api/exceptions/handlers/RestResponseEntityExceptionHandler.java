package com.pandora.api.exceptions.handlers;

import com.pandora.api.exceptions.rest.BaseRestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {


    @ExceptionHandler({BaseRestException.class})
    public ResponseEntity<Object> handleBaseException(
            BaseRestException ex, WebRequest request) {
        HttpStatus code = ex.getStatus();
        return new ResponseEntity<>(
                new Response(ex, ex.getStatus().value()), new HttpHeaders(), code);
    }
}