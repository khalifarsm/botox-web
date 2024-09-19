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
    private Long redemptions;
    private Long used;
    private String accountId;
    private Long transactionId;
    private Long ownerId;
    private Long paymentId;

    public Long getDays() {
        return duration / (1000 * 60 * 60 * 24);
    }
}
