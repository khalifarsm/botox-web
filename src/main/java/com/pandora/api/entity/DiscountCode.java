package com.pandora.api.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "botox_discount_codes")
@Accessors(chain = true)
@Data
public class DiscountCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Date created;
    private int discount;
    private int minAccounts;
    private long used = 0L;
    private Long adminId;

    public Long getAdminId() {
        if (adminId == null) return -1L;
        return adminId;
    }
}
