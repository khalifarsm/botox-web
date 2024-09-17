package com.pandora.api.exceptions.rest;

import org.springframework.http.HttpStatus;

public class WipedAccountRestException extends BaseRestException {
    public WipedAccountRestException(String message) {
        super(message);
        this.setStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
    }
}
