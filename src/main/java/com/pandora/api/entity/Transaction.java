package com.pandora.api.entity;

import com.pandora.api.dto.TransactionDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "botox_orders")
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String address;
    private Double amount;
    private String qrUrl;
    private Integer confirmsNeeded;
    private Long timeout;
    private String duration;
    private Integer accounts;
    private String coupon;
    private Long price;
    private String status;
    @Column(name = "show_transaction")
    private boolean showDetails = true;
    private Date created;

    public Transaction(TransactionDTO that) {
        this.transactionId = that.getTransactionId();
        this.address = that.getAddress();
        this.confirmsNeeded = that.getConfirmsNeeded();
        this.qrUrl = that.getQrUrl();
        this.timeout = that.getTimeout();
        this.amount = that.getAmount();
        this.duration = that.getDuration();
        this.price = that.getPrice();
        this.accounts = that.getAccounts();
        this.coupon = that.getCoupon();
        this.status = that.getStatus();
        this.created = new Date();
    }
}
