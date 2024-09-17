package com.pandora.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegisterRequestDTO {
    private String subscription;
    private String reset;
    private String password;
}
