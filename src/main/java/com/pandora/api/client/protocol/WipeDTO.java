package com.pandora.api.client.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WipeDTO {
    private String wipeCode;
    private String pandoraId;
}
