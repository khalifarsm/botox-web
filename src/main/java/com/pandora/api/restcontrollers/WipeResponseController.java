package com.pandora.api.restcontrollers;

import com.pandora.api.entity.Account;
import com.pandora.api.exceptions.rest.UnauthorizedRestException;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.websocket.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class WipeResponseController {

    private final AuthenticationService authenticationService;
    private final AccountRepository accountRepository;

    @GetMapping("/wipe")
    public void wipeResponse(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            throw new UnauthorizedRestException("authentication required");
        }
        if (authorization.startsWith("Basic ")) {
            authorization = authorization.split(" ",2)[1];
        }
        String userId = authenticationService.validate(authorization);
        Account account = accountRepository.findFirstByUserId(userId).orElseThrow(()->new UnauthorizedRestException("authentication failed"));
        account.setResponseDate(new Date());
        accountRepository.save(account);
    }
}
