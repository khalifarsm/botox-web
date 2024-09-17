package com.pandora.api.dto;

import com.pandora.api.entity.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.brunocvcunha.coinpayments.model.CreateTransactionResponse;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
public class TransactionDTO {
    private Long orderId;
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
    private Set<String> codes;
    private Long countDown;

    public TransactionDTO(CreateTransactionResponse that) {
        this.transactionId = that.getTransactionId();
        this.address = that.getAddress();
        this.confirmsNeeded = that.getConfirmsNeeded();
        this.qrUrl = that.getQrcodeUrl();
        this.timeout = that.getTimeout();
        this.amount = that.getAmount();
    }

    public TransactionDTO(Transaction that) {
        this.orderId = that.getId();
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
        if (that.getCreated() != null) {
            Long timePassed = (new Date().getTime() - that.getCreated().getTime()) / 1000;
            this.countDown = that.getTimeout() - timePassed;
            if (this.countDown < 0) {
                this.countDown = 0L;
            }
        }
    }
}
