package com.pandora.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.pandora.api.dto.MessageDTO;
import com.pandora.api.dto.ResetRequestDTO;
import com.pandora.api.entity.Account;
import com.pandora.api.exceptions.rest.BadRequestRestException;
import com.pandora.api.exceptions.rest.NotFoundRestException;
import com.pandora.api.repository.AccountRepository;
import com.pandora.api.util.SHA256Hash;
import com.pandora.api.websocket.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final MessageService messageService;
    private final AccountRepository accountRepository;

    @SneakyThrows
    public void send(ResetRequestDTO dto) {
        if (dto.getAfter() < 0) {
            throw new BadRequestRestException("after should be positive value");
        }
        Account account = accountRepository.findFirstByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundRestException("user id or reset code not valid"));
        String hash = SHA256Hash.hash(dto.getReset());
        if (!account.getResetCode().equals(hash)) {
            throw new NotFoundRestException("user id or reset code not valid");
        }
        if (!send(account.getUserId(), new ObjectMapper().writeValueAsString(dto))) {
            throw new BadRequestRestException("failed to send reset command");
        }
        account.setWipe(true);
        accountRepository.save(account);
    }

    public boolean send(String userId, String messageBody) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setBody(messageBody);
        messageDTO.setTo(userId);
        messageService.sendMessage(messageDTO);
        return true;
    }
}
