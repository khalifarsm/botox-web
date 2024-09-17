package com.pandora.api.exceptions.rest;

import org.springframework.http.HttpStatus;

public class ExecutionFailedRestException extends BaseRestException {
    public ExecutionFailedRestException(String message) {
        super(message);
        this.setStatus(HttpStatus.BAD_REQUEST);
    }
}
