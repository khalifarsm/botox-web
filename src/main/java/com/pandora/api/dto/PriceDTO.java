package com.pandora.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PriceDTO {
    private Long price;
}
