package com.pandora.api.exceptions.rest;

import org.springframework.http.HttpStatus;

public class BadRequestRestException extends BaseRestException {
    public BadRequestRestException(String message) {
        super(message);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
