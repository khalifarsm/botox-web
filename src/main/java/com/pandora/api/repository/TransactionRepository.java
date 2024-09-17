package com.pandora.api.repository;

import com.pandora.api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findFirstByTransactionId(String code);
    Transaction findFirstByAddress(String address);
    Transaction findFirstByTransactionIdAndShowDetailsAndStatus(String address,boolean show,String status);

    Optional<Transaction> findFirstByIdAndShowDetails(Long transactionId, boolean show);
}
