package com.pandora.api.exceptions.handlers;

import lombok.Data;

import java.util.Date;

@Data
public class Response {
    private Integer status;
    private String message;
    private Long timestamp;

    public Response(RuntimeException ex, Integer status) {
        this.status = status;
        message = ex.getMessage();
        timestamp = new Date().getTime();
    }
}
