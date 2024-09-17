package com.pandora.api.security;

import com.pandora.api.entity.Account;
import com.pandora.api.exceptions.rest.UnauthorizedRestException;
import com.pandora.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtUserDetailsService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public JwtUserDetailsService() {
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findFirstByUserId(username).orElseThrow(()->new UnauthorizedRestException("user cannot be found : "+username));
        return account;
    }

    public UserDetails auth(String username, String password) {
        Optional<Account> account = accountRepository.findFirstByUserId(username);
        if (!account.isPresent()) {
            throw new UnauthorizedRestException("wrong password or id");
        }
        if (!passwordEncoder.matches(password, account.get().getPassword())) {
            throw new UnauthorizedRestException("wrong password or id");
        }
        return account.get();
    }


    public Account getAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String currentPrincipalName = authentication.getName();
        Account account = accountRepository.findFirstByUserId(currentPrincipalName)
                .orElseThrow(()->new UnauthorizedRestException("user not found"));
        return account;
    }
}
