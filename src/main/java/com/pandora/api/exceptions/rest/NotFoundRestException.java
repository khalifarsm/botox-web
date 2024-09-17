package com.pandora.api.exceptions.rest;

import org.springframework.http.HttpStatus;

public class NotFoundRestException extends BaseRestException {
    public NotFoundRestException(String message) {
        super(message);
        this.setStatus(HttpStatus.NOT_FOUND);
    }
}
