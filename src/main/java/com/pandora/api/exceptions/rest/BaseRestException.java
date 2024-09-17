package com.pandora.api.exceptions.rest;

import org.springframework.http.HttpStatus;

public class BaseRestException extends RuntimeException {

    private HttpStatus status;

    public BaseRestException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
