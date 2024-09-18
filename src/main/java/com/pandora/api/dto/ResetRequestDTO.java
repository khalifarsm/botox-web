package com.pandora.api.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
public class ResetRequestDTO {
    private String reset;
    private String userId;
    private Long after;
}
