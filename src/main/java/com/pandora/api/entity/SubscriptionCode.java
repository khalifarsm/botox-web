package com.pandora.api.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "botox_subscription_codes")
@Accessors(chain = true)
@Data
public class SubscriptionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 12)
    private String code;
    private Long duration;
    private Date created;
    private Date usageDate;
    private boolean used = false;
    private String accountId;
    private Long transactionId;
    private Long ownerId;

    public Long getDays() {
        return duration / (1000 * 60 * 60 * 24);
    }
}
