package com.pandora.api.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private String body;
    private String to;

}
