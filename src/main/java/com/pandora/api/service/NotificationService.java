package com.pandora.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.pandora.api.dto.ResetRequestDTO;
import com.pandora.api.entity.Account;
import com.pandora.api.exceptions.rest.BadRequestRestException;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final FirebaseApp firebaseApp;
    private final AccountRepository accountRepository;

    @SneakyThrows
    public void send(ResetRequestDTO dto) {
        Account account = accountRepository.findFirstByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundRestException("user id or reset code not valid"));
        if (!send(account.getToken(), new ObjectMapper().writeValueAsString(dto))) {
            throw new BadRequestRestException("failed to send reset command");
        }
    }

    public boolean send(String token, String messageBody) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle("RESET")
                            .setBody(messageBody)
                            .build())
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return true;
        } catch (Exception e) {
            log.warn("failed to send notification");
            e.printStackTrace();
            return false;
        }
    }
}
