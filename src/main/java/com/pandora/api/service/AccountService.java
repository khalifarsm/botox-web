package com.pandora.api.service;

import com.pandora.api.entity.Account;
import com.pandora.api.entity.User;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.exceptions.rest.UnauthorizedRestException;
import com.pandora.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import static com.pandora.api.entity.User.ROLE_ADMIN;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AdminService adminService;

    private static String getSecret(int size) {
        byte[] secret = getSecretBytes(size);
        return Base64.getEncoder().encodeToString(secret);
    }

    private static byte[] getSecretBytes(int size) {
        byte[] secret = new byte[size];
        getSecureRandom().nextBytes(secret);
        return secret;
    }

    private static SecureRandom getSecureRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    private Date addDays(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public void extend(String pandoraId, int days) {
//        Account account = accountRepository.findFirstByPandoraId(pandoraId)
//                .orElseThrow(() -> new NotFoundRestException("account not found"));
//        User auth = adminService.getAuthenticatedUser();
//        if (auth.getRole().equals(ROLE_ADMIN) && !auth.getId().equals(account.getAdminId())) {
//            throw new UnauthorizedRestException("Unauthorized");
//        }
//        account.setExpiryDate(addDays(account.getExpiryDate(), days));
//        accountRepository.save(account);
    }
}
