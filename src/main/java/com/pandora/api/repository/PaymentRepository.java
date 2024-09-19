package com.pandora.api.repository;

import com.pandora.api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findFirstByUid(String code);
}
