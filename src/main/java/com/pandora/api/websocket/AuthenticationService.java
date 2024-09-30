package com.pandora.api.websocket;

import com.pandora.api.entity.Account;
import com.pandora.api.exceptions.rest.UnauthorizedRestException;
import com.pandora.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public String validate(String requestTokenHeader) {
        UnauthorizedRestException ex = new UnauthorizedRestException("authentication failed");
        String data = new String(Base64.getDecoder().decode(requestTokenHeader));
        if (data.split(":", 2).length < 2) {
            throw ex;
        }
        String userId = data.split(":", 2)[0];
        String password = data.split(":", 2)[1];
        Account account = accountRepository.findFirstByUserId(userId).orElseThrow(() -> ex);
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw ex;
        }
        return userId;
    }
}
