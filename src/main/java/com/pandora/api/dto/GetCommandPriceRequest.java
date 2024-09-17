package com.pandora.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetCommandPriceRequest {
    private String duration;
    private Integer accounts;
    private String coupon;
}
