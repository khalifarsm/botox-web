package com.pandora.api.repository;

import com.pandora.api.entity.SubscriptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SubscriptionCodeRepository extends JpaRepository<SubscriptionCode, Long> {
    Set<SubscriptionCode> findByTransactionId(Long transactionId);
    Page<SubscriptionCode> findByOwnerId(Long id, Pageable pageable);

    Long countByOwnerId(Long id);
}
