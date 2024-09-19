package com.pandora.api.service;

import com.pandora.api.entity.Payment;
import com.pandora.api.entity.SubscriptionCode;
import com.pandora.api.entity.Transaction;
import com.pandora.api.entity.User;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.repository.SubscriptionCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionCodeService {

    private final SubscriptionCodeRepository subscriptionCodeRepository;

    public SubscriptionCode create(SubscriptionCode code, Long userId) {
        code.setCode(generateRandomString(12));
        code.setCreated(new Date());
        code.setOwnerId(userId);
        code.setUsed(0L);
        return subscriptionCodeRepository.save(code);
    }

    public SubscriptionCode create(Payment payment) {
        SubscriptionCode code = subscriptionCodeRepository.findFirstByPaymentId(payment.getId());
        if (code == null) {
            code = new SubscriptionCode();
            code.setRedemptions(1L);
            code.setCode(generateRandomString(12));
            code.setCreated(new Date());
            code.setOwnerId(null);
            code.setUsed(0L);
            code.setPaymentId(payment.getId());
            return subscriptionCodeRepository.save(code);
        } else {
            return code;
        }

    }

    public List<SubscriptionCode> create(Transaction transaction) {
        List<SubscriptionCode> codes = new ArrayList<>();
        for (int i = 0; i < transaction.getAccounts(); i++) {
            SubscriptionCode code = new SubscriptionCode();
            code.setCode(generateRandomString(12));
            code.setCreated(new Date());
            long duration = Integer.valueOf(transaction.getDuration().replace("M", "")) * 30 * 24 * 60 * 60 * 1000;
            code.setDuration(duration);
            code.setUsed(0L);
            code.setTransactionId(transaction.getId());
            codes.add(code);
        }
        return subscriptionCodeRepository.saveAll(codes);
    }

    public Page<SubscriptionCode> list(int page) {
        Page<SubscriptionCode> list = subscriptionCodeRepository.findAll(PageRequest.of(page - 1, 100, Sort.Direction.DESC, "id"));
        return list;
    }

    public Page<SubscriptionCode> list(int page, Long id) {
        Page<SubscriptionCode> list = subscriptionCodeRepository.findByOwnerId(id, PageRequest.of(page - 1, 100, Sort.Direction.DESC, "id"));
        return list;
    }

    private String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            randomString.append(randomChar);
        }
        return randomString.toString();
    }

    public void delete(Long id) {
        SubscriptionCode subscriptionCode = subscriptionCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundRestException("code not found"));
        subscriptionCodeRepository.delete(subscriptionCode);
    }
}
