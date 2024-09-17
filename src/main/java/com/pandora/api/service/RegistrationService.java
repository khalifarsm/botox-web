package com.pandora.api.service;

import com.pandora.api.dto.RegisterRequestDTO;
import com.pandora.api.dto.RegisterResponseDTO;
import com.pandora.api.entity.Account;
import com.pandora.api.entity.SubscriptionCode;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.repository.SubscriptionCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final SubscriptionCodeRepository subscriptionCodeRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponseDTO register(RegisterRequestDTO dto) {
        SubscriptionCode code = subscriptionCodeRepository.findFirstByCode(dto.getSubscription());
        if (code == null) {
            throw new NotFoundRestException("Subscription code is not valid");
        }
        if (code.getRedemptions() <= code.getUsed()) {
            throw new NotFoundRestException("Subscription code already used");
        }
        String userId = generateRandomString(6);
        while (accountRepository.findFirstByUserId(userId).isPresent()) {
            userId = generateRandomString(6);
        }
        Account account = new Account();
        account.setResetCode(dto.getReset());
        account.setWipe(false);
        account.setUserId(userId);
        account.setToken(dto.getToken());
        account.setAdminId(code.getOwnerId());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        accountRepository.save(account);

        code.setUsed(code.getUsed() + 1);
        code.setAccountId(userId);
        subscriptionCodeRepository.save(code);
        return new RegisterResponseDTO().setUserId(userId);
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
}
