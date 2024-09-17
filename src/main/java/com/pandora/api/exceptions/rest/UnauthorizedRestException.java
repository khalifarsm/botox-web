package com.pandora.api.exceptions.rest;

import org.springframework.http.HttpStatus;

public class UnauthorizedRestException extends BaseRestException {
    public UnauthorizedRestException(String message) {
        super(message);
        this.setStatus(HttpStatus.UNAUTHORIZED);
    }
}
