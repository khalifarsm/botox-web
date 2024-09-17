package com.pandora.api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GetCommandPriceResponse {
    private String duration;
    private Integer accounts;
    private String coupon;

    private Long unitPrice;
    private Long totalPrice;
    private Long finalPrice;
}
